package com.runninghi.runninghibackv2.common.exception.custom;

public class CustomEntityNotFoundException extends RuntimeException{
    public CustomEntityNotFoundException(final String message) { super(message);}

    public CustomEntityNotFoundException(final String message, final Object object) {
        super(String.format(message + "- request info => %s", object));
    }
}
