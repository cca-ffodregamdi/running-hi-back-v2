package com.runninghi.runninghibackv2.common.exception.custom;

public class AdminInvalidPasswordException extends RuntimeException{
    public AdminInvalidPasswordException(){}
    public AdminInvalidPasswordException(String message) {
        super(message);
    }
}
