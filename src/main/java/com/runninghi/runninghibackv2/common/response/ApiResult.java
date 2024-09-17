package com.runninghi.runninghibackv2.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiResult<T>(
        @Schema(description = "응답 시간", example = "2024-03-27T14:20:52.026425")
        LocalDateTime timeStamp,
        @Schema(description = "응답 상태", example = "OK")
        HttpStatus status,
        @Schema(description = "응답 메시지", example = "성공 응답 메시지 예시")
        String message,
        @Schema(description = "응답 데이터")
        T data
) {

    public static <T> ApiResult<T> success(String errorMessage, T data) {
        return new ApiResult<>(LocalDateTime.now(), HttpStatus.OK, errorMessage, data);
    }

    public static <T> ApiResult<T> error(ErrorCode errorCode) {
        return new ApiResult<>(LocalDateTime.now(), errorCode.getStatus(), errorCode.getMessage(), null);
    }

    public static <T> ApiResult<T> error(HttpStatus httpStatus, String errorMessage) {
        return new ApiResult<>(LocalDateTime.now(), httpStatus, errorMessage, null);
    }

    public static <T> ApiResult<T> error(HttpStatus httpStatus, String errorMessage, T data) {
        return new ApiResult<>(LocalDateTime.now(), httpStatus, errorMessage, data);
    }

}

