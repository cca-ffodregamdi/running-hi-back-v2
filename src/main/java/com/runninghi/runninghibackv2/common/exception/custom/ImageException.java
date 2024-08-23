package com.runninghi.runninghibackv2.common.exception.custom;

public class ImageException extends RuntimeException{
    public ImageException() {}
    public ImageException(String message) {
        super(message);
    }
    public ImageException(String message, Throwable cause) {
        super(message, cause);
    }
}

