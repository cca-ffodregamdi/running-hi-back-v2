package com.runninghi.runninghibackv2.common.exception.custom;

public class KakaoOauthException extends RuntimeException {

    public KakaoOauthException() {
    }
    public KakaoOauthException(String message) {
        super(message);
    }

    public KakaoOauthException(String message, Throwable cause) {
        super(message, cause);
    }
}
