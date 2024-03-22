package com.runninghi.runninghibackv2.member.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.member.application.dto.response.KakaoProfileResponse;
import com.runninghi.runninghibackv2.member.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    private final MemberService memberService;

    @GetMapping("/login/kakao")
    public String kakaoLogin() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", kakaoRedirectUri)
                .queryParam("response_type", "code");

        return "redirect:" + builder.toUriString();
    }

    @PostMapping("/login/kakao/callback")
    public ResponseEntity<ApiResult> kakaoCallback(@RequestParam String code) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        String accessTokenRequestUrl = "https://kauth.kakao.com/oauth/token";
        Map<String, String> response = restTemplate.postForObject(accessTokenRequestUrl, params, Map.class);

        String kakaoAccessToken = response != null ? response.get("access_token") : null;
        KakaoProfileResponse kakaoProfileResponse = memberService.getKakaoProfile(kakaoAccessToken);
        Map<String, String> tokens = memberService.loginWithKakao(kakaoProfileResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokens.get("accessToken"));
        headers.add("Refresh-Token", tokens.get("refreshToken"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResult.success("Success Kakao Login", null));
    }

}
