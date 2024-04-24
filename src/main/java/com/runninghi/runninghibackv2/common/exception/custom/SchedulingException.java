package com.runninghi.runninghibackv2.common.exception.custom;

public class SchedulingException extends RuntimeException{
    public SchedulingException() {
    }

    public SchedulingException(String message) {
        super(message);
    }

    public SchedulingException(String message, Throwable cause) {
        super(message, cause);
    }
}
