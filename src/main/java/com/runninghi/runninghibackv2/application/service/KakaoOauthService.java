package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.common.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.AccessTokenInfo;
import com.runninghi.runninghibackv2.common.dto.RefreshTokenInfo;
import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.common.exception.custom.KakaoOauthException;
import com.runninghi.runninghibackv2.application.dto.member.response.KakaoProfileResponse;
import com.runninghi.runninghibackv2.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoOauthService {

    private static final int NICKNAME_DIGIT_LENGTH = 8;
    private static final int RANDOM_NUMBER_RANGE = 10;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.current-version-uri}")
    private String currentVersionUri;

    @Value("${kakao.admin-key}")
    private String adminKey;

    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    private final SecureRandom random = new SecureRandom();


    /**
     * 카카오 인증 페이지의 URI를 생성하여 반환합니다.
     *
     * @return 카카오 인증 페이지의 URI
     */
    public String getKakaoUri() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", currentVersionUri + kakaoRedirectUri)
                .queryParam("response_type", "code");

        return builder.toUriString();
    }

    /**
     * 카카오 인가 코드를 사용하여 OAuth를 처리하고, 인증된 사용자의 정보를 반환합니다.
     *
     * @param code 카카오로부터 받은 인가 코드
     * @return 인증된 사용자의 액세스 토큰 및 리프레시 토큰
     * @throws KakaoLoginException 카카오 로그인 처리 중 오류가 발생한 경우 예외가 발생합니다.
     */
    @Transactional
    public Map<String, String> kakaoOauth(String code) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", currentVersionUri + kakaoRedirectUri);
        params.add("code", code);

        String accessTokenRequestUrl = "https://kauth.kakao.com/oauth/token";
        Map<String, String> response = restTemplate.postForObject(accessTokenRequestUrl, params, Map.class);

        String kakaoAccessToken = response != null ? response.get("access_token") : null;
        if (kakaoAccessToken == null) {
            throw new KakaoOauthException();
        }

        KakaoProfileResponse kakaoProfileResponse = getKakaoProfile(kakaoAccessToken);
        if (kakaoProfileResponse == null) {
            throw new KakaoOauthException();
        }
        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoProfileResponse.getKakaoId().toString());

        return optionalMember.map(this::loginWithKakao).orElseGet(() -> loginWithKakaoCreateMember(kakaoProfileResponse));
    }

    /**
     * 카카오 사용자의 프로필 정보를 가져옵니다.
     *
     * @param kakaoAccessToken 카카오 액세스 토큰
     * @return 카카오 사용자의 프로필 정보
     */
    KakaoProfileResponse getKakaoProfile(String kakaoAccessToken){
        // user 정보를 가져오는 kakao api url
        String url = "https://kapi.kakao.com/v2/user/me";

        // http header 설정 : access token 을 넣어서 user 정보에 접근할 수 있도록 한다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        // 필요한 사용자 정보 가져오기
        MultiValueMapAdapter<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"id\", \"kakao_account.email\", \"properties.nickname\"]");

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, request, KakaoProfileResponse.class);
    }

    /**
     * 카카오 사용자의 프로필 정보를 사용하여 로그인 처리를 수행하고, 액세스 토큰 및 리프레시 토큰을 반환합니다.
     *
     * @param member 로그인할 회원 정보
     * @return 액세스 토큰 및 리프레시 토큰
     */
    private Map<String, String> loginWithKakao(Member member) {
        AccessTokenInfo accessTokenInfo = new AccessTokenInfo(member.getMemberNo(), member.getRole());
        RefreshTokenInfo refreshTokenInfo = new RefreshTokenInfo(member.getKakaoId(), member.getRole());

        String refreshToken = jwtTokenProvider.createRefreshToken(refreshTokenInfo);
        String accessToken = jwtTokenProvider.createAccessToken(accessTokenInfo);

        member.updateRefreshToken(refreshToken);
        member.activateMember();  // 멤버의 활성화 상태를 true로 변경, deactivateDate를 null로 설정
        memberRepository.save(member);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    /**
     * 카카오 프로필을 기반으로 새로운 회원을 생성하고, 회원의 액세스 토큰과 리프레시 토큰을 반환합니다.
     *
     * @param kakaoProfile 카카오 프로필 정보
     * @return 액세스 토큰과 리프레시 토큰으로 구성된 맵
     */
    private Map<String, String> loginWithKakaoCreateMember(KakaoProfileResponse kakaoProfile) {
        Member member = Member.builder()
                .kakaoId(kakaoProfile.getKakaoId().toString())
                .kakaoName(kakaoProfile.getNickname())
                .nickname("러너 " + generateRandomDigits())
                .isActive(true)
                .isBlacklisted(false)
                .role(Role.USER)
                .distanceToNextLevel(0)
                .totalKcal(0.0)
                .totalDistance(0.0)
                .level(0)
                .build();

        // 리프레시 토큰 생성
        RefreshTokenInfo refreshTokenInfo = new RefreshTokenInfo(member.getKakaoId(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(refreshTokenInfo);

        member.updateRefreshToken(refreshToken);

        // 멤버 저장, 액세스 토큰 생성
        Member savedMember = memberRepository.saveAndFlush(member);
        AccessTokenInfo accessTokenInfo = new AccessTokenInfo(savedMember.getMemberNo(), savedMember.getRole());
        String accessToken = jwtTokenProvider.createAccessToken(accessTokenInfo);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    /**
     * 회원의 카카오 계정을 연결 해제하고 회원을 삭제합니다.
     *
     * @param memberNo 회원 번호
     * @return 회원의 활성화 상태를 토글한 결과 (활성화되었으면 true, 비활성화되었으면 false)
     * @throws EntityNotFoundException 요청된 회원 번호에 해당하는 회원을 찾을 수 없을 때 발생하는 예외
     * @throws KakaoUnlinkException 카카오 API 호출이 실패한 경우 발생하는 예외
     */
    @Transactional
    public boolean unlinkAndDeleteMember(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);

        // 연결을 끊는 kakao api url
        String url = "https://kapi.kakao.com/v1/user/unlink";

        // http header 설정 : access token 을 넣어서 user 정보에 접근할 수 있도록 한다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", member.getKakaoId());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =  restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new KakaoOauthException();
        }

        // 멤버의 활성화 상태를 false로 변경, deactivateDate 설정
        member.deactivateMember();
        memberRepository.save(member);

        return member.isActive();
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
