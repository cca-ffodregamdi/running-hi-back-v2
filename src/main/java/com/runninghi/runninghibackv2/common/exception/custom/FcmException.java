package com.runninghi.runninghibackv2.common.exception.custom;

public class FcmException extends RuntimeException{
    public FcmException() {}

    public FcmException(String detailMessage) {
        super(detailMessage);
    }

    public FcmException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
