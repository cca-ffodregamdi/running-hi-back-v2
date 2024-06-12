package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.member.response.AppleTokenResponse;
import com.runninghi.runninghibackv2.auth.apple.*;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.exception.custom.AppleOauthException;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.vo.RunDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppleOauthService {

    private static final int NICKNAME_DIGIT_LENGTH = 8;
    private static final int RANDOM_NUMBER_RANGE = 10;
    private static final String GRANT_TYPE = "authorization_code";

    private final AppleTokenParser appleTokenParser;
    private final AppleClient appleClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleClaimsValidator appleClaimsValidator;
    private final AppleClientSecretProvider appleClientSecretProvider;

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final SecureRandom random = new SecureRandom();

    @Value("${apple.client-id}")
    private String clientId;

    // client secret 생성
    @Transactional
    public String createClientSecret() {
        try {
            return appleClientSecretProvider.createClientSecret();
        } catch (Exception e) {
            throw new AppleOauthException("apple client secret 생성에 실패했습니다. : " + e.getMessage());
        }
    }

    // apple id_token 요청
    @Transactional
    public AppleTokenResponse getAppleToken(String code, String clientSecret) {
        try {
            return appleClient.appleAuth(clientId, code, GRANT_TYPE, clientSecret);
        } catch (Exception e) {
            throw new AppleOauthException("apple token 요청에 실패했습니다. : " + e.getMessage());
        }
    }

    // apple user 생성
    @Transactional
    public Map<String, String> appleOauth(AppleTokenResponse appleTokenResponse, String nonce) {
        // id_token의 header를 추출
        Map<String, String> appleTokenHeader = appleTokenParser.parseHeader(appleTokenResponse.idToken());

        // it_token을 검증하기 위해 애플의 publicKey list 요청
        ApplePublicKeys applePublicKeys = appleClient.getApplePublicKeys();

        // publicKey list에서 id_token과 alg와 kid가 일치하는 publicKey를 찾기
        // 해당 publicKey로 it_token 추출에 사용할 RSApublicKey 생성
        PublicKey publicKey = applePublicKeyGenerator.generate(appleTokenHeader, applePublicKeys);

        // id_token을 publicKey로 검증하여 claim 추출 : 서명 검증
        Claims claims = appleTokenParser.extractClaims(appleTokenResponse.idToken(), publicKey);

        // iss, aud, exp, nonce 검증
        if (!appleClaimsValidator.isValid(claims, nonce)) {
            throw new AppleOauthException("Apple Claims 유효성 검사 실패 : 잘못된 apple 토큰입니다.");
        }

        Map<String, String> appleResponse = extractAppleResponse(claims);

        Optional<Member> optionalMember = memberRepository.findByAppleId(appleResponse.get("sub"));

        return optionalMember.map(this::loginWithApple)
                .orElseGet(() -> loginWithAppleCreateMember(appleResponse, appleTokenResponse.refreshToken()));
    }

    // id-token에서 sub, name 추출
    @Transactional
    public Map<String, String> extractAppleResponse(Claims claims) {
        Map<String, String> appleResponse = new HashMap<>();

        appleResponse.put("sub", Objects.toString(claims.get("sub"), "N/A"));
        appleResponse.put("name", Objects.toString(claims.get("name"), "N/A"));

        return appleResponse;
    }

    // 애플 연결해제, 회원 탈퇴 처리
    @Transactional
    public boolean unlinkAndDeleteMember(Long memberNo, String clientSecret) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);

        String authorization = "Bearer " + clientSecret;

        appleClient.revokeToken(authorization, clientId, member.getAppleRefreshToken(), "refresh_token");

        member.deactivateMember();
        memberRepository.save(member);

        return member.isActive();
    }

    // 새로운 토큰 생성 & 반환
    private Map<String, String> generateTokens(Member member) {
        AccessTokenInfo accessTokenInfo = new AccessTokenInfo(member.getMemberNo(), member.getRole());
        RefreshTokenInfo refreshTokenInfo = new RefreshTokenInfo(member.getAppleId(), member.getRole());

        String refreshToken = jwtTokenProvider.createRefreshToken(refreshTokenInfo);
        String accessToken = jwtTokenProvider.createAccessToken(accessTokenInfo);

        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    // 로그인 메서드
    private Map<String, String> loginWithApple(Member member) {
        member.activateMember();  // 멤버의 활성화 상태를 true로 변경, deactivateDate를 null로 설정
        return generateTokens(member);
    }

    // 회원 생성 및 로그인 메서드
    private Map<String, String> loginWithAppleCreateMember(Map<String, String> appleResponse, String appleRefreshToken) {

        Member member = Member.builder()
                .appleId(appleResponse.get("sub"))
                .name(appleResponse.get("name"))
                .appleRefreshToken(appleRefreshToken)
                .nickname("러너 " + generateRandomDigits())
                .isActive(true)
                .isBlacklisted(false)
                .role(Role.USER)
                .runDataVO(new RunDataVO(0.0,0.0,10,0))
                .build();

        memberRepository.saveAndFlush(member);
        return generateTokens(member);
    }

    /**
     * 무작위 숫자로 구성된 문자열을 생성하여 반환합니다.
     *
     * @return 생성된 무작위 숫자 8자리 문자열
     */
    private String generateRandomDigits() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < NICKNAME_DIGIT_LENGTH; i++) {
            stringBuilder.append(random.nextInt(RANDOM_NUMBER_RANGE));
        }

        return stringBuilder.toString();
    }

}
