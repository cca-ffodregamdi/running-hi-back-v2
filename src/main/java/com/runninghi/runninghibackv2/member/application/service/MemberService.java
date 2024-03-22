package com.runninghi.runninghibackv2.member.application.service;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.dto.MemberJwtInfo;
import com.runninghi.runninghibackv2.common.entity.Role;
import com.runninghi.runninghibackv2.member.application.dto.response.KakaoProfileResponse;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.MemberRefreshToken;
import com.runninghi.runninghibackv2.member.domain.repository.MemberRefreshTokenRepository;
import com.runninghi.runninghibackv2.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;

    @Transactional
    public KakaoProfileResponse getKakaoProfile(String kakaoAccessToken){
        // user 정보를 가져오는 kakao api url
        String url = "https://kapi.kakao.com/v2/user/me";

        // http header 설정 : access token 을 넣어서 user 정보에 접근할 수 있도록 한다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 필요한 사용자 정보 가져오기
        MultiValueMapAdapter<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"id\", \"kakao_account.email\", \"properties.nickname\"]");

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, request, KakaoProfileResponse.class);

    }

    @Transactional
    public Map<String, String> loginWithKakao(KakaoProfileResponse kakaoProfile) {
        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoProfile.kakaoId().toString());

        Map<String, String> tokens = new HashMap<>();
        Member member;

        if (optionalMember.isPresent()) {
            member = optionalMember.get();

            MemberJwtInfo memberJwtInfo = new MemberJwtInfo(member.getMemberNo(), member.getRole());

            String refreshToken = jwtTokenProvider.createRefreshToken(memberJwtInfo);
            String accessToken = jwtTokenProvider.createAccessToken(memberJwtInfo);

            MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository.findByMember(member);
            memberRefreshToken.updateRefreshToken(refreshToken);

            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

        } else {
            member = Member.builder()
                    .kakaoId(kakaoProfile.getKakaoId().toString())
                    .kakaoName(kakaoProfile.getNickname())
                    .role(Role.USER)
                    .build();

            MemberJwtInfo memberJwtInfo = new MemberJwtInfo(member.getMemberNo(), member.getRole());

            String refreshToken = jwtTokenProvider.createRefreshToken(memberJwtInfo);
            String accessToken = jwtTokenProvider.createAccessToken(memberJwtInfo);

            MemberRefreshToken refreshTokenEntity = MemberRefreshToken.builder()
                    .member(member)
                    .refreshToken(refreshToken)
                    .build();

            memberRefreshTokenRepository.save(refreshTokenEntity);

            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

        }

        return tokens;
    }


}
