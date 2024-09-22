package com.runninghi.runninghibackv2.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"COMMON ERR-404-PAGE","페이지를 찾을 수 없습니다."),
    INTER_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON ERR-500","서버 오류입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON ERR-400", "잘못된 요청입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "COMMON ERR-403", "권한이 없습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON ERR-404-ENTITY", "해당 ID에 대한 엔티티를 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "COMMON ERR-401-TOKEN", "유효하지않은 토큰입니다."),

    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "COMMON ERR-400-VALIDATION", "입력값이 올바른 형식이 아닙니다."),
    SCHEDULING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "SCHEDULING ERR-500", "스케줄링 오류가 발생했습니다."),
    FCM_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON ERR-500-FCM", "FCM 전송에 실패하였습니다."),

    KAKAO_OAUTH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "KAKAO ERR-500-KAKAO", "카카오 OAuth 오류입니다."),
    KAKAO_OAUTH_UNLINK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "KAKAO ERR-500-KAKAO-UNLINK", "카카오 OAuth 연결 해제 오류입니다."),
    KAKAO_OAUTH_PROFILE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "KAKAO ERR-500-KAKAO-PROFILE", "카카오 OAuth 프로필을 가져올 때 오류가 발생했습니다."),

    APPLE_OAUTH_CLIENT_SECRET_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "APPLE ERR-500-APPLE-CLIENT-SECRET", "Apple Client-Secret 생성에 실패했습니다."),
    APPLE_OAUTH_TOKEN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "APPLE ERR-500-APPLE-TOKEN", "Apple Refresh-Token 요청에 실패했습니다."),
    APPLE_OAUTH_CLAIMS_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "APPLE ERR-500-APPLE-CLAIMS", "Apple Claims 유효성 검사에 실패했습니다. 잘못된 Apple 토큰입니다."),
    APPLE_OAUTH_UNLINK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "APPLE ERR-500-APPLE-UNLINK", "Apple Oauth 연결 해제 오류입니다."),
    APPLE_OAUTH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "APPLE ERR-500-APPLE", "Apple OAuth 오류입니다."),
    APPLE_PUBLIC_KEY_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLE ERR-404-PUBLIC-KEY", "매칭되는 public key를 찾을 수 없습니다."),
    APPLE_PUBLIC_KEY_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "APPLE ERR-500-PUBLIC-KEY-GENERATOR", "RSAPublicKey 생성 중 오류가 발생했습니다."),

    FEEDBACK_INVALID_DATA(HttpStatus.BAD_REQUEST, "FEEDBACK ERR-400-FEEDBACK", "피드백 : 데이터가 유효하지 않습니다."),

    ADMIN_INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "ADMIN ERR-401-ADMIN-SIGNIN", "관리자 비밀번호가 잘못되었습니다."),
    ADMIN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "ADMIN ERR-401-ADMIN-UNAUTHORIZED", "관리자 권한이 없습니다."),
    ADMIN_ACCOUNT_DUPLICATED(HttpStatus.CONFLICT, "ADMIN ERR-409-SIGNUP", "이미 가입된 관리자 계정입니다."),
    ADMIN_SIGNUP_INVALID_INVITATION(HttpStatus.BAD_REQUEST, "ADMIN ERR-400-INVITATION", "잘못된 관리자 초대 코드입니다."),
    MEMBER_INVALID_DATA(HttpStatus.BAD_REQUEST, "MEMBER ERR-400-DATA", "멤버 : 데이터가 유효하지않습니다."),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}