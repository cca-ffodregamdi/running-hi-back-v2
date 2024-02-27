package com.runninghi.runninghibackv2.common.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiResult(
        LocalDateTime timeStamp,
        HttpStatus status,
        String message,
        Object data
) {

    public static ApiResult success(String message, Object data) {
        return new ApiResult(LocalDateTime.now(), HttpStatus.OK, message, data);
    }

    public static ApiResult error(ErrorCode errorCode) {
        String errorMessage = errorCode.getCode() + " : " + errorCode.getMessage();
        return new ApiResult(LocalDateTime.now(), errorCode.getStatus(), errorMessage, null);
    }

}