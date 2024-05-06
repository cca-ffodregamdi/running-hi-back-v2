package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.member.request.UpdateMemberInfoRequest;
import com.runninghi.runninghibackv2.application.dto.member.response.GetMemberResponse;
import com.runninghi.runninghibackv2.application.dto.member.response.UpdateMemberInfoResponse;
import com.runninghi.runninghibackv2.application.service.KakaoOauthService;
import com.runninghi.runninghibackv2.application.service.MemberService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
     * 카카오 로그인 페이지로 리다이렉트합니다.
     *
     * <p>이 API는 사용자를 카카오 로그인 페이지로 리다이렉트 시키는 역할을 합니다. 사용자가 카카오 계정으로 로그인을 시도할 때 사용됩니다.
     * 로그인이 성공적으로 완료되면, 카카오는 사용자를 /api/v1/login/kakao/callback 엔드포인트로 리다이렉트하며, 인가 코드를 함께 전달합니다.
     * 이 인가 코드를 통해 서버는 카카오 서버로부터 사용자의 회원 정보를 받아와 회원가입 또는 로그인 처리를 진행합니다.
     * 처리가 완료되면, 사용자에게 로그인 성공 여부와 함께 액세스 토큰 및 리프레시 토큰이 반환됩니다.</p>
     */
    @GetMapping("/api/v1/login/kakao")
    @Operation(summary = "카카오 회원가입/로그인",
            description = "카카오 로그인 페이지로 리다이렉트합니다. 사용자가 카카오 계정으로 로그인하면, 서버는 리다이렉트하여 인가 코드를 처리합니다. " +
                    "회원 가입 처리가 완료되면 최종적으로 헤더에 엑세스 토큰과 리프레시 토큰 정보를 넣어서 클라이언트에 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success Kakao Login",
                            headers = {
                                    @Header(name = "Authorization", description = "Access Token", schema = @Schema(type = "string")),
                                    @Header(name = "Refresh-Token", description = "Refresh Token", schema = @Schema(type = "string"))
                            },
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResult.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> kakaoLogin() {

        URI kakaoUri = URI.create(kakaoOauthService.getKakaoUri());

        return ResponseEntity.status(HttpStatus.FOUND).location(kakaoUri).build();
    }

    /**
     * 카카오 로그인 콜백을 처리하는 API 엔드포인트입니다.
     *
     * <p>카카오로부터 리다이렉트된 요청을 처리하며, 인가 코드를 포함하고 있습니다.
     * 이 코드를 사용하여 카카오 서버에서 사용자 정보를 받아온 후, 이를 통해 사용자의 회원가입 또는 로그인 처리를 진행합니다.
     * 처리가 완료되면, 로그인 성공 여부와 함께 액세스 토큰 및 리프레시 토큰을 헤더에 포함하여 클라이언트에게 반환합니다.</p>
     *
     * @param code 카카오로부터 받은 인가 코드. 로그인 성공 시 카카오로부터 전달받습니다.
     * @return 로그인 성공 여부 및 인증 토큰 정보를 포함하는 ResponseEntity 객체.
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
     * 카카오 계정과의 연결을 해제하고 탈퇴 처리하는 API입니다.
     *
     * <p>이 API는 사용자가 카카오 서비스와의 연결을 해제하고, 해당 사용자의 회원 정보를 탈퇴 처리합니다.
     * 사용자는 Bearer 토큰을 통해 인증되어야 하며, 해당 토큰은 요청 헤더에 포함되어야 합니다.
     * 성공적으로 처리되면, 사용자는 카카오 서비스와의 연결이 해제되며, 회원 탈퇴가 진행됩니다.</p>
     *
     * @param token 사용자 인증을 위한 Bearer 토큰. 요청 헤더에서 'Authorization'으로 제공되어야 합니다. 필수 파라미터입니다.
     * @return ResponseEntity 객체에 담긴 ApiResult, 성공 여부를 Boolean 값으로 반환.
     * @apiNote 이 API는 카카오와의 연결 해제 및 회원 탈퇴를 위해 사용자 인증이 필요합니다. 성공적으로 연결 해제 및 탈퇴가 완료되면, "Success Kakao Unlink" 메시지와 함께 성공 여부가 반환됩니다.
     */
    @PutMapping("/api/v1/unlink/kakao")
    @Operation(summary = "카카오 회원 탈퇴", description = "카카오 서비스와 연결을 끊고 회원 탈퇴를 진행합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
            },
            responses = { @ApiResponse(responseCode = "200", description = "Success Kakao Unlink") }
    )
    public ResponseEntity<ApiResult<Boolean>> kakaoLogout(@RequestHeader(value = "Authorization") String token) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        boolean isActive = kakaoOauthService.unlinkAndDeleteMember(memberNo);

        return ResponseEntity.ok()
                .body(ApiResult.success("Success Kakao Unlink", isActive));
    }

    /**
     * 회원 정보를 수정하는 API입니다.
     *
     * <p>이 API는 회원의 정보를 수정하는 기능을 제공합니다. 현재 구현된 기능은 닉네임 수정입니다.
     * 사용자는 Bearer 토큰을 통해 인증된 후에만 이 API를 사용할 수 있습니다. 토큰은 요청 헤더에 포함되어야 합니다.<p>
     * @param token 사용자 인증을 위한 Bearer 토큰. 요청 헤더에서 'Authorization'으로 제공되어야 합니다. 필수 파라미터입니다.
     * @param request 회원 정보 수정 요청 데이터를 담고 있는 객체. 현재는 닉네임을 수정하기 위한 정보를 포함합니다.
     *                요청 본문에 포함되어야 합니다.
     * @return ResponseEntity 객체를 통해 ApiResult<UpdateMemberInfoResponse> 타입의 응답을 반환합니다.
     *      응답 본문에는 회원 정보 수정 성공 메시지와 함께, 수정된 정보가 포함됩니다.
     */
    @PutMapping("/api/v1/members")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다. 현재는 닉네임만 수정할 수 있습니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "회원 정보 업데이트 성공", content = @Content(schema = @Schema(implementation = ApiResult.class))) }
    )
    public ResponseEntity<ApiResult<UpdateMemberInfoResponse>> updateMemberInfo(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody UpdateMemberInfoRequest request
    ) throws BadRequestException {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        UpdateMemberInfoResponse response = memberService.updateMemberInfo(memberNo, request);

        return ResponseEntity.ok(ApiResult.success("회원 정보 업데이트 성공", response));
    }

    /**
     * 회원 정보를 조회하는 API입니다.
     *
     * <p>이 메서드를 통해 사용자는 자신의 회원 정보를 조회할 수 있습니다.
     * 사용자 인증을 위해 Bearer 토큰이 요청 헤더에 포함되어야 합니다.</p>
     *
     * @param token 사용자 인증을 위한 Bearer 토큰. 요청 헤더에 포함되어야 합니다.
     * @return ResponseEntity 객체를 통해 ApiResult<GetMemberResponse> 타입의 응답을 반환합니다.
     *         응답 본문에는 회원 정보 조회 성공 메시지와 함께, 조회된 회원의 상세 정보가 포함됩니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 인증되지 않았을 경우 접근이 거부됩니다.
     */
    @GetMapping("/api/v1/members")
    @Operation(summary = "회원 정보 조회", description = "사용자 정보를 조회합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공", content = @Content(schema = @Schema(implementation = ApiResult.class))) }
    )
    public ResponseEntity<ApiResult<GetMemberResponse>> getMemberInfo(@RequestHeader(value = "Authorization") String token) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        GetMemberResponse response = memberService.getMemberInfo(memberNo);

        return ResponseEntity.ok(ApiResult.success("회원 정보 조회 성공", response));

    }

    /**
     * FCM 토큰과 사용자의 알림 수신 동의 여부를 저장하는 API입니다.
     *
     * <p>이 메서드는 사용자의 FCM 토큰과 알림 수신 동의 여부를 저장합니다. 사용자 인증은 요청 헤더에 포함된 Bearer 토큰을 통해 이루어집니다.
     * 사용자가 앱에서 푸시 알림을 받기 위해 동의한 경우, 해당 정보를 시스템에 저장하여 알림 서비스를 제공할 수 있습니다.</p>
     *
     * @param token 사용자 인증을 위한 Bearer 토큰. 요청 헤더에 포함되어야 합니다.
     * @param fcmToken 사용자의 FCM 토큰. 푸시 알림을 받기 위해 필요한 토큰입니다. 요청 헤더에 포함되어야 합니다.
     * @param alarmConsent 사용자의 알림 수신 동의 여부. URL 경로에 포함된 값으로, 사용자가 알림 수신을 동의한 경우 true, 동의하지 않은 경우 false가 됩니다.
     * @return ResponseEntity 객체를 통해 ApiResult 타입의 응답을 반환합니다. 응답 본문에는 "FCM 토큰 저장 성공" 메시지가 포함됩니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 Bearer 토큰과 FcmToken이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 인증되지 않았을 경우 접근이 거부됩니다.
     */
    @PutMapping("/api/v1/member/fcmToken/{alarmConsent}")
    @Operation(summary = "FCM 토큰 저장", description = "FCM 토큰과 알림 수신 동의 여부를 저장합니다.")
    public ResponseEntity<ApiResult> saveFCMToken(@RequestHeader(value = "Authorization") String token,
                                                  @RequestHeader(value = "FcmToken") String fcmToken,
                                                  @PathVariable(value = "alarmConsent") boolean alarmConsent) {

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);

        memberService.saveFCMToken(memberNo, fcmToken, alarmConsent);

        return ResponseEntity.ok().body(ApiResult.success("FCM 토큰 저장 성공", null));
    }

}
