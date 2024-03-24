package com.runninghi.runninghibackv2.member.application.controller;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.member.application.service.KakaoLoginService;
import com.runninghi.runninghibackv2.member.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final KakaoLoginService kakaoLoginService;


    /**
     * 카카오 로그인 요청을 처리하는 API 엔드포인트입니다.
     *
     * @return 카카오 로그인 페이지의 URI
     */
    @GetMapping("/api/v1/login/kakao")
    public ResponseEntity<ApiResult> kakaoLogin() {

        String kakaoUri = kakaoLoginService.getKakaoUri();

        return ResponseEntity.ok(ApiResult.success("Redirect Success", kakaoUri));
    }

    /**
     * 카카오 로그인 콜백을 처리하는 API 엔드포인트입니다.
     *
     * @param code 카카오로부터 받은 인가 코드
     * @return 로그인 성공 여부 및 인증 토큰 정보
     */
    @GetMapping("api/v1/login/kakao/callback")
    public ResponseEntity<ApiResult> kakaoCallback(@RequestParam("code") String code) {

        Map<String, String> tokens = kakaoLoginService.kakaoOauth(code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokens.get("accessToken"));
        headers.add("Refresh-Token", tokens.get("refreshToken"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResult.success("Success Kakao Login", null));
    }

}
