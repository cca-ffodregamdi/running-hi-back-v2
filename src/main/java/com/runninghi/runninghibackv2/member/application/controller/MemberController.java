package com.runninghi.runninghibackv2.member.application.controller;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.member.application.dto.request.UpdateMemberInfoRequest;
import com.runninghi.runninghibackv2.member.application.dto.response.GetMemberResponse;
import com.runninghi.runninghibackv2.member.application.dto.response.UpdateMemberInfoResponse;
import com.runninghi.runninghibackv2.member.application.service.KakaoOauthService;
import com.runninghi.runninghibackv2.member.application.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원 API", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final KakaoOauthService kakaoOauthService;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * 카카오 로그인 요청을 처리하는 API 엔드포인트입니다.
     *
     * @return 카카오 로그인 페이지로 리다이렉트
     */
    @GetMapping("/api/v1/login/kakao")
    @Operation(summary = "카카오 로그인/회원가입", description = "카카오 OAuth 서비스를 이용하여 로그인/회원가입을 진행합니다.")
    public ResponseEntity<Void> kakaoLogin() {

        URI kakaoUri = URI.create(kakaoOauthService.getKakaoUri());

        return ResponseEntity.status(HttpStatus.FOUND).location(kakaoUri).build();
    }

    /**
     * 카카오 로그인 콜백을 처리하는 API 엔드포인트입니다.
     *
     * @param code 카카오로부터 받은 인가 코드
     * @return 로그인 성공 여부 및 인증 토큰 정보
     */
    @RequestMapping("/api/v1/login/kakao/callback")
    @Operation(hidden = true)
    public ResponseEntity<ApiResult<Void>> kakaoCallback(@RequestParam("code") String code) {

        Map<String, String> tokens = kakaoOauthService.kakaoOauth(code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokens.get("accessToken"));
        headers.add("Refresh-Token", tokens.get("refreshToken"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResult.success("Success Kakao Login", null));
    }

    /**
     * 카카오 Unlink 엔드포인트입니다. 토큰을 제공하여 로그인된 회원의 카카오 계정을 연결 해제하고 탈퇴합니다.
     *
     * @param token 인증 토큰
     * @return member의 활성화 상태인 isActvie(boolean) 값를 포함하는 ResponseEntity 객체
     */
    @PutMapping("/api/v1/unlink/kakao")
    @Operation(summary = "카카오 회원 탈퇴", description = "카카오 서비스와 연결을 끊고 회원 탈퇴를 진행합니다.")
    public ResponseEntity<ApiResult<Boolean>> kakaoLogout(@RequestHeader(value = "Authorization") String token) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        boolean isActive = kakaoOauthService.unlinkAndDeleteMember(memberNo);

        return ResponseEntity.ok()
                .body(ApiResult.success("Success Kakao Unlink", isActive));
    }

    @PutMapping("/api/v1/members")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    public ResponseEntity<ApiResult<UpdateMemberInfoResponse>> updateMemberInfo(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody UpdateMemberInfoRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        UpdateMemberInfoResponse response = memberService.updateMemberInfo(memberNo, request);

        return ResponseEntity.ok(ApiResult.success("회원 정보 업데이트 성공", response));
    }

    @GetMapping("/api/v1/members")
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    public ResponseEntity<ApiResult<GetMemberResponse>> getMemberInfo(@RequestHeader(value = "Authorization") String token) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        GetMemberResponse response = memberService.getMemberInfo(memberNo);

        return ResponseEntity.ok(ApiResult.success("회원 정보 조회 성공", response));

    }
}
