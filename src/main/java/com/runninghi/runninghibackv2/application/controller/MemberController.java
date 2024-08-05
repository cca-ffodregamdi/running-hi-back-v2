package com.runninghi.runninghibackv2.application.controller;

import com.runninghi.runninghibackv2.application.dto.member.request.*;
import com.runninghi.runninghibackv2.application.dto.member.response.*;
import com.runninghi.runninghibackv2.application.service.AppleOauthService;
import com.runninghi.runninghibackv2.application.service.KakaoOauthService;
import com.runninghi.runninghibackv2.application.service.MemberService;
import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.common.exception.custom.InvalidTokenException;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "회원 API", description = "회원 관련 API")
public class MemberController {
    
    private final MemberService memberService;
    private final KakaoOauthService kakaoOauthService;
    private final AppleOauthService appleOauthService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 카카오 회원가입/로그인 API 엔드포인트입니다.
     * <p>
     * 카카오 토큰을 검증한 후 회원가입 또는 로그인 처리합니다.
     * 처리가 완료되면, 사용자에게 로그인 성공 여부와 함께 액세스 토큰 및 리프레시 토큰이 반환됩니다.</p>
     */
    @PostMapping(value = "/api/v1/login/kakao", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "카카오 회원가입/로그인",
            description = "회원 가입/로그인 처리가 완료되면 헤더에 엑세스 토큰과 리프레시 토큰 정보를 넣어서 클라이언트에 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success Kakao Login",
                            headers = {
                                    @Header(name = "Authorization", description = "Access Token", schema = @Schema(type = "string")),
                                    @Header(name = "Refresh-Token", description = "Refresh Token", schema = @Schema(type = "string"))
                            }
                    )
            }
    )
    public ResponseEntity<ApiResult<CreateMemberResponse>> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        log.info("카카오 로그인 요청이 들어왔습니다. 카카오 토큰: {}", request.kakaoToken());

        Map<String, String> memberResponse = kakaoOauthService.kakaoOauth(request.kakaoToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", memberResponse.get("accessToken"));
        headers.add("Refresh-Token", memberResponse.get("refreshToken"));

        CreateMemberResponse response = CreateMemberResponse.from(memberResponse.getOrDefault("memberNo", null));
        log.info("카카오 로그인 성공. 회원 번호: {}", response.memberNo());

        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResult.success("성공적인 카카오 로그인", response));
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
    @PutMapping(value = "/api/v1/unlink/kakao", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "카카오 회원 탈퇴", description = "카카오 서비스와 연결을 끊고 회원 탈퇴를 진행합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true),
            },
            responses = { @ApiResponse(responseCode = "200", description = "Success Kakao Unlink") }
    )
    public ResponseEntity<ApiResult<Boolean>> kakaoUnlink(@RequestHeader(value = "Authorization") String token) {
        log.info("카카오 연결 해제 요청이 들어왔습니다. 토큰: {}", token);

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
        log.debug("추출된 멤버 번호: {}", memberNo);

        boolean isActive = kakaoOauthService.unlinkAndDeleteMember(memberNo);
        log.info("카카오 연결 해제 성공. 회원 번호: {}", memberNo);

        return ResponseEntity.ok()
                .body(ApiResult.success("성공적인 카카오 연결 해제", isActive));
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
    @PutMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다. 현재는 닉네임만 수정할 수 있습니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "회원 정보 업데이트 성공") }
    )
    public ResponseEntity<ApiResult<UpdateMemberInfoResponse>> updateMemberInfo(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody UpdateMemberInfoRequest request
    ) throws BadRequestException {
        log.info("회원 정보 수정 요청이 들어왔습니다. 토큰: {}", token);

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
        log.debug("추출된 멤버 번호: {}", memberNo);

        UpdateMemberInfoResponse response = memberService.updateMemberInfo(memberNo, request);

        log.info("회원 정보 수정 성공. 회원 번호: {}", memberNo);

        return ResponseEntity.ok(ApiResult.success("회원 정보 업데이트 성공", response));
    }


    /**
     * 애플 로그인 또는 회원가입을 처리하는 API입니다.
     *
     * <p>이 API는 사용자의 애플 계정으로 로그인하거나 회원가입을 처리합니다.
     * 클라이언트는 애플 인증 코드와 nonce를 요청 본문에 담아서 해당 엔드포인트를 호출해야 합니다.
     * 인증이 성공하면 응답 헤더에 액세스 토큰과 리프레시 토큰이 포함됩니다.</p>
     *
     * @param request AppleLoginRequest 객체로, 애플 인증 코드와 nonce를 포함합니다.
     * @return ResponseEntity 객체를 통해 ApiResult 타입의 응답을 반환합니다. 인증이 성공하면 응답 헤더에 액세스 토큰과 리프레시 토큰이 포함됩니다.
     */
    @PostMapping(value = "/api/v1/login/apple", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "애플 로그인 또는 회원가입",
            description = "사용자의 애플 계정으로 로그인하거나 회원가입을 처리합니다. " +
                    "클라이언트는 애플 인증 코드와 nonce를 요청 본문에 담아서 해당 엔드포인트를 호출해야 합니다. " +
                    "인증이 성공하면 응답 헤더에 액세스 토큰과 리프레시 토큰이 포함됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success Apple Login",
                    headers = {
                            @Header(name = "Authorization", description = "Access Token", schema = @Schema(type = "string")),
                            @Header(name = "Refresh-Token", description = "Refresh Token", schema = @Schema(type = "string"))
                    })
    })
    public ResponseEntity<ApiResult<CreateMemberResponse>> appleLogin(@RequestBody AppleLoginRequest request) {
        log.info("애플 로그인 요청이 들어왔습니다. 인증 코드: {}", request.authorizationCode());

        // clientSecret 생성
        String clientSecret = appleOauthService.createClientSecret();
        log.info("애플 로그인용 클라이언트 시크릿 생성됨");

        // authorizationCode와 clientSecret으로 refreshToken 가져오기
        AppleTokenResponse appleTokenResponse = appleOauthService.getAppleToken(request.authorizationCode(), clientSecret);

        // 클라이언트에서 전달받은 identityToken 검증, 회원가입/로그인 처리
        Map<String, String> memberResponse = appleOauthService.appleOauth(request, appleTokenResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", memberResponse.get("accessToken"));
        headers.add("Refresh-Token", memberResponse.get("refreshToken"));

        CreateMemberResponse response = CreateMemberResponse.from(memberResponse.getOrDefault("memberNo", null));

        log.info("애플 로그인 성공. 회원 번호: {}", response.memberNo());

        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResult.success("Success Apple Login", response));
    }

    /**
     * 애플 회원 탈퇴를 처리하는 API입니다.
     *
     * <p>이 API는 사용자의 애플 계정과 연동된 회원 정보를 탈퇴 처리합니다.
     * 클라이언트는 Authorization 헤더에 Bearer 토큰을 포함하여 해당 엔드포인트를 호출해야 합니다.
     * 탈퇴가 성공하면 응답 본문에 true 값을 반환합니다.</p>
     *
     * @param token 사용자의 인증을 위한 Bearer 토큰입니다.
     * @return ResponseEntity 객체를 통해 ApiResult 타입의 응답을 반환합니다. 회원 탈퇴가 성공하면 true 값을 반환합니다.
     */
    @PutMapping(value = "/api/v1/unlink/apple", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "애플 회원 탈퇴",
            description = "사용자의 애플 계정과 연동된 회원 정보를 탈퇴 처리합니다. " +
                    "클라이언트는 Authorization 헤더에 Bearer 토큰을 포함하여 해당 엔드포인트를 호출해야 합니다. " +
                    "탈퇴가 성공하면 응답 본문에 true 값을 반환합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success Apple Unlink"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public ResponseEntity<ApiResult<Boolean>> appleUnlink(@RequestHeader(value = "Authorization") String token) throws InterruptedException {
        log.info("애플 회원 탈퇴 요청을 받았습니다. 토큰: {}", token);

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
        log.debug("추출된 멤버 번호: {}", memberNo);

        String clientSecret = appleOauthService.createClientSecret();
        log.debug("애플 탈퇴를 위한 클라이언트 시크릿 생성됨.");

        boolean isActive = appleOauthService.unlinkAndDeleteMember(memberNo, clientSecret);
        log.info("애플 탈퇴 처리 완료. 회원 번호: {}", memberNo);

        return ResponseEntity.ok(ApiResult.success("Success Apple Unlink", isActive));
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
    @GetMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원 정보 조회", description = "사용자 정보를 조회합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공") }
    )
    public ResponseEntity<ApiResult<GetMemberResponse>> getMemberInfo(@RequestHeader(value = "Authorization") String token) {
        log.info("회원 정보 조회 요청을 받았습니다. 토큰: {}", token);

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
        log.debug("추출된 멤버 번호: {}", memberNo);

        GetMemberResponse response = memberService.getMemberInfo(memberNo);
        log.info("회원 정보 조회 성공.");

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
    @PutMapping(value = "/api/v1/member/fcmToken/{alarmConsent}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "FCM 토큰 저장", description = "FCM 토큰과 알림 수신 동의 여부를 저장합니다.")
    public ResponseEntity<ApiResult<Void>> saveFCMToken(@RequestHeader(value = "Authorization") String token,
                                                  @RequestHeader(value = "FcmToken") String fcmToken,
                                                  @PathVariable(value = "alarmConsent") boolean alarmConsent) {
        log.info("FCM 토큰 저장 요청을 받았습니다. 토큰: {}, FcmToken: {}, 알림 동의 여부: {}", token, fcmToken, alarmConsent);

        Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
        log.debug("추출된 멤버 번호: {}", memberNo);

        memberService.saveFCMToken(memberNo, fcmToken, alarmConsent);
        log.info("FCM 토큰 및 알림 동의 여부 저장 완료.");

        return ResponseEntity.ok().body(ApiResult.success("FCM 토큰 저장 성공", null));
    }


    /**
     * 자동 로그인의 액세스 토큰 유효성 검사 API입니다.
     *
     * <p>이 메서드는 액세스 토큰의 유효성을 검사하여 자동 로그인을 처리합니다. 유효한 토큰인 경우 성공 응답을 반환하고, 만료된 토큰인 경우 적절한 에러 메시지를 반환합니다.</p>
     *
     * @param token 검사할 액세스 토큰. 요청 헤더에 "Authorization" 키로 포함되어야 합니다.
     * @return ResponseEntity 객체를 통해 ApiResult 타입의 응답을 반환합니다. 응답 본문에는 토큰 유효성 검사 결과가 포함됩니다.
     *          유효한 토큰은 true, 만료된 토큰은 false, 서명이 일치하지않는 토큰은 null 값이 응답 본문에 포함됩니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 액세스 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 만료된 경우 적절한 에러 응답을 반환합니다.
     */
    @PostMapping(value = "/api/v1/login/access-token/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "자동 로그인의 엑세스 토큰 유효성 검사",
            description = "리프레시 토큰의 유효성을 검사하여 자동 로그인을 처리하고 새로운 액세스 토큰을 발급합니다. 응답 본문에는 토큰 유효성 검사 결과가 포함됩니다.\n" +
                            "유효한 토큰은 true, 만료된 토큰은 false, 서명이 일치하지않는 토큰은 null 값이 응답 본문에 포함됩니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Bearer 토큰", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "자동 로그인 : 엑세스 토큰 유효성 검사 성공"),
                    @ApiResponse(responseCode = "403", description = "자동 로그인 : 만료된 엑세스 토큰입니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timeStamp\": \"2024-05-23T07:36:26.119Z\", \"status\": \"FORBIDDEN\", \"message\": \"자동 로그인 : 만료된 엑세스 토큰입니다.\", \"data\": false}"))),
                    @ApiResponse(responseCode = "401", description = "자동 로그인 : 유효하지않은 엑세스 토큰입니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"timeStamp\": \"2024-05-23T07:36:26.119Z\", \"status\": \"UNAUTHORIZED\", \"message\": \"자동 로그인 : 유효하지않은 엑세스 토큰입니다.\", \"data\": null}"))),
            })
    public ResponseEntity<ApiResult<Boolean>> autoLoginWithAccessToken(@RequestHeader(value = "Authorization") String token) {
        log.info("액세스 토큰 유효성 검사 요청을 받았습니다. 토큰: {}", token);

        try {
            if (jwtTokenProvider.validateAutoLoginAccessToken(token)) {
                log.info("액세스 토큰이 유효합니다.");
                return ResponseEntity.ok(ApiResult.success("자동 로그인 : 엑세스 토큰 유효성 검사 성공", true));
            } else {
                log.warn("액세스 토큰이 만료되었습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResult.error(HttpStatus.FORBIDDEN, "자동 로그인 : 만료된 엑세스 토큰입니다.", false));
            }
        } catch (InvalidTokenException e) {
            log.error("액세스 토큰이 유효하지 않습니다.", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResult.error(HttpStatus.UNAUTHORIZED, "자동 로그인 : 유효하지않은 엑세스 토큰입니다."));
        }
    }


    /**
     * 리프레시 토큰의 유효성을 검사하여 자동 로그인을 처리하고 새로운 액세스 토큰을 발급하는 API입니다.
     *
     * <p>이 메서드는 사용자의 리프레시 토큰을 검사하여 유효한 경우 새로운 액세스 토큰을 발급합니다. 만료되었거나 유효하지 않은 경우 적절한 응답을 반환합니다.</p>
     *
     * @param refreshToken 사용자 인증을 위한 리프레시 토큰. 요청 헤더에 "Refresh-Token" 키로 포함되어야 합니다.
     * @return ResponseEntity 객체를 통해 ApiResult 타입의 응답을 반환합니다. 토큰이 유효한 경우 true값이 응답 본문에 들어가며, 새로운 액세스 토큰이 응답 헤더에 포함됩니다.
     *          토큰이 만료된 경우 false, 서명이 유효하지않은 경우 null 값이 응답 본문에 포함됩니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 리프레시 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 인증되지 않았을 경우 접근이 거부됩니다.
     */
    @PostMapping(value = "/api/v1/login/refresh-token/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "자동 로그인의 리프레시 토큰 유효성 검사 및 액세스 토큰 갱신",
            description = "리프레시 토큰의 유효성을 검사하여 자동 로그인을 처리하고 새로운 액세스 토큰을 발급합니다. " +
                    "토큰이 유효한 경우 true 값이 응답 본문에 들어가며, 새로운 액세스 토큰이 응답 헤더에 포함됩니다.\n" +
                    "토큰이 만료된 경우 false, 서명이 유효하지않은 경우 null 값이 응답 본문에 포함됩니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Refresh-Token", description = "사용자 인증을 위한 Refresh 토큰", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "자동 로그인 : 리프레시 토큰 유효성 검사 성공. 새로운 액세스 토큰 발급"),
                          @ApiResponse(responseCode = "403", description = "자동 로그인 : 만료된 리프레시 토큰입니다. 다시 로그인해주세요.",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timeStamp\": \"2024-05-23T07:36:26.119Z\", \"status\": \"FORBIDDEN\", \"message\": \"자동 로그인 : 만료된 리프레시 토큰입니다. 다시 로그인해주세요.\", \"data\": false}"))),
                          @ApiResponse(responseCode = "401", description = "자동 로그인 : 유효하지않은 리프레시 토큰입니다.",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"timeStamp\": \"2024-05-23T07:36:26.119Z\", \"status\": \"UNAUTHORIZED\", \"message\": \"자동 로그인 : 유효하지않은 리프레시 토큰입니다.\", \"data\": null}"))),
            })
    public ResponseEntity<ApiResult<Boolean>> autoLoginWithRefreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        log.info("리프레시 토큰 유효성 검사 및 액세스 토큰 갱신 요청을 받았습니다. 리프레시 토큰: {}", refreshToken);

        try {
            if (jwtTokenProvider.validateAutoLoginRefreshToken(refreshToken)) {
                log.info("리프레시 토큰이 유효합니다. 새로운 액세스 토큰 발급.");
                String newAccessToken = jwtTokenProvider.renewAccessToken(refreshToken);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + newAccessToken);

                return ResponseEntity.ok()
                        .headers(headers)
                        .body((ApiResult.success("새로운 액세스 토큰 발급", true)));
            } else {
                log.warn("리프레시 토큰이 만료되었습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResult.error(HttpStatus.FORBIDDEN, "자동 로그인 : 만료된 토큰입니다. 다시 로그인해주세요.", false));
            }
        } catch (InvalidTokenException e) {
            log.error("리프레시 토큰이 유효하지 않습니다.", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResult.error(HttpStatus.UNAUTHORIZED, "자동 로그인 : 유효하지않은 토큰입니다.", null));
        }
    }


    /**
     * 액세스 토큰에서 멤버 번호를 추출하여 반환하는 API입니다.
     *
     * @param token 사용자 인증을 위한 액세스 토큰. 요청 헤더에 "Authorization" 키로 포함되어야 합니다.
     * @return ResponseEntity 객체를 통해 ApiResult 타입의 응답을 반환합니다. 토큰이 유효한 경우 멤버 번호가 응답 본문에 들어가며, 유효하지 않은 경우 null 값이 응답 본문에 포함됩니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 액세스 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 인증되지 않았을 경우 접근이 거부됩니다.
     */
    @GetMapping(value = "/api/v1/member/id", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "액세스 토큰 유효성 검사 및 멤버 번호 반환",
            description = "액세스 토큰의 유효성을 검사하여 멤버 번호를 추출합니다. " +
                    "토큰이 유효한 경우 멤버 번호가 응답 본문에 들어갑니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Access 토큰", required = true)
            },
            responses = { @ApiResponse(responseCode = "200", description = "토큰 유효성 검사 성공. 멤버 번호 반환",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetMemberNoResponse.class))),
            })
    public ResponseEntity<ApiResult<GetMemberNoResponse>> getMemberNoFromToken(@RequestHeader(value = "Authorization") String token) {
        log.info("액세스 토큰 유효성 검사 및 멤버 번호 반환 요청을 받았습니다. 토큰: {}", token);

        try {
            Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
            log.debug("추출된 멤버 번호: {}", memberNo);

            GetMemberNoResponse response = GetMemberNoResponse.from(memberNo);

            log.info("멤버 번호 조회 성공. 멤버 번호: {}", memberNo);
            return ResponseEntity.ok(ApiResult.success("회원 id 조회 성공", response));

        } catch (Exception e) {
            log.error("액세스 토큰 유효성 검사 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResult.error(HttpStatus.UNAUTHORIZED, "액세스 토큰 유효성 검사 중 오류 발생"));
        }
    }

    /**
     * 사용자의 현재 위치를 업데이트하거나 새로 생성하는 API입니다.
     *
     * @param token 사용자 인증을 위한 액세스 토큰. 요청 헤더에 "Authorization" 키로 포함되어야 합니다.
     * @param request 사용자의 현재 위치 정보 (latitude, longitude 좌표)
     * @return ResponseEntity 객체를 통해 ApiResult 타입의 응답을 반환합니다. 위치 정보 업데이트 또는 생성이 성공한 경우 저장된 위치 정보가 응답 본문에 포함됩니다.
     * @apiNote 이 메서드를 사용하기 위해서는 요청 헤더에 유효한 액세스 토큰이 포함되어야 합니다.
     *          토큰이 유효하지 않거나, 토큰에 해당하는 사용자가 인증되지 않았을 경우 접근이 거부됩니다.
     */
    @PutMapping(value = "/api/v1/member/geometry", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "사용자 위치 정보 업데이트 또는 생성",
            description = "사용자의 현재 위치를 업데이트하거나 새로 생성합니다. " +
                    "이미 위치 정보가 있는 경우 업데이트하고, 없는 경우 새로 생성합니다.",
            parameters = {
                    @Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "사용자 인증을 위한 Access 토큰", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "위치 정보 업데이트 또는 생성 성공",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateCurrentLocationResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            })
    public ResponseEntity<ApiResult<UpdateCurrentLocationResponse>> updateOrCreateLocation(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody UpdateCurrentLocationRequest request) {

        log.info("사용자 위치 정보 업데이트 또는 생성 요청을 받았습니다. 좌표: ({}, {})", request.latitude(), request.longitude());

        try {
            Long memberNo = jwtTokenProvider.getMemberNoFromToken(token);
            log.debug("토큰에서 추출한 멤버 번호: {}", memberNo);

            UpdateCurrentLocationResponse response = memberService.updateCurrentLocation(memberNo, request);

            return ResponseEntity.ok(ApiResult.success("위치 정보 업데이트 성공", response));

        } catch (Exception e) {
            log.error("위치 정보 업데이트 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResult.error(HttpStatus.UNAUTHORIZED, "인증 실패 또는 위치 정보 업데이트 중 오류 발생"));
        }
    }

    /**
     * 관리자가 로그인하여 JWT 토큰을 발급받는 API입니다.
     *
     * @param request 관리자의 사용자 이름과 비밀번호를 포함한 로그인 요청 데이터
     * @return ResponseEntity 객체를 통해 ApiResult 타입의 응답을 반환합니다. 로그인 성공 시 JWT 토큰이 응답 본문에 포함됩니다.
     * @apiNote 이 메서드는 로그인 시 유효한 사용자 이름과 비밀번호를 포함해야 하며, 인증 실패 시 적절한 오류 메시지를 반환합니다.
     */
    @PostMapping("/api/v1/login/admin")
    @Operation(summary = "관리자 페이지 로그인",
            description = "관리자가 로그인하여 JWT 토큰을 발급받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "관리자 로그인 성공, JWT 토큰 발급",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdminLoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "인증 실패, 잘못된 관리자 이름 또는 비밀번호")
            })
    public ResponseEntity<ApiResult<AdminLoginResponse>> login(@RequestBody AdminLoginRequest request) {
        log.info("관리자 로그인 요청: 사용자 account = {}", request.account());

        try {
            Map<String, String> tokens = memberService.adminLogin(request);
            log.info("관리자 로그인 성공: 사용자 account = {}", request.account());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", tokens.get("accessToken"));
            headers.add("Refresh-Token", tokens.get("refreshToken"));

            AdminLoginResponse response = new AdminLoginResponse(jwtTokenProvider.getMemberNoFromToken(tokens.get("accessToken")));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(ApiResult.success("관리자 로그인 성공", response));
        } catch (Exception e) {
            log.error("관리자 로그인 실패: 사용자 account = {}", request.account(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResult.error(HttpStatus.UNAUTHORIZED, "잘못된 사용자 이름 또는 비밀번호"));
        }
    }
}
