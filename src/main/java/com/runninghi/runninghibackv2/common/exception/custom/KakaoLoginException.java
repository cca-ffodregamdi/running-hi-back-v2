package com.runninghi.runninghibackv2.common.exception.custom;

public class KakaoLoginException extends RuntimeException {

    public KakaoLoginException() {
    }
    public KakaoLoginException(String message) {
        super(message);
    }

    public KakaoLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
