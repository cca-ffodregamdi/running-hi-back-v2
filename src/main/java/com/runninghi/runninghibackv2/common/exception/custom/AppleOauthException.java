package com.runninghi.runninghibackv2.common.exception.custom;

public class AppleOauthException extends RuntimeException {

    public AppleOauthException() {}
    public AppleOauthException(String message) {
        super(message);
    }

    public AppleOauthException(String message, Throwable cause) {
        super(message, cause);
    }
}
