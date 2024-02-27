package com.runninghi.runninghibackv2.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"COMMON ERR-404","페이지를 찾을 수 없습니다."),
    INTER_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON ERR-500","해당 ID는 존재하지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON ERR-400", "잘못된 요청입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}