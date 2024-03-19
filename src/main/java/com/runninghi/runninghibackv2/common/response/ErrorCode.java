package com.runninghi.runninghibackv2.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"COMMON ERR-404-PAGE","페이지를 찾을 수 없습니다."),
    INTER_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON ERR-500","해당 ID는 존재하지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON ERR-400", "잘못된 요청입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "COMMON ERR-403", "권한이 없습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON ERR-404-ENTITY", "해당 ID에 대한 엔티티를 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "COMMON ERR-401-TOKEN", "유효하지않은 토큰입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}