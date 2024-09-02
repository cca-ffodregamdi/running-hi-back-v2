package com.runninghi.runninghibackv2.common.exception.custom;

public class ImageException extends RuntimeException{
    public ImageException(String message) {
        super(message);
    }
    public ImageException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static class InvalidFileName extends ImageException {

        public InvalidFileName(final String message) {super(message);}

        public InvalidFileName(final String message, final Object object) {
            super(String.format(message + "- request info => %s", object));
        }
    }

    public static class UnSupportedImageTypeException extends ImageException {

        public UnSupportedImageTypeException(final String message) { super(message); }

        public UnSupportedImageTypeException(final String message, final Object object) {
            super(String.format(message + "- request info => %s", object));
        }
    }

    public static class InvalidImageLength extends ImageException {

        public InvalidImageLength(final String message) { super(message); }

        public InvalidImageLength(final String message, final Object object) {
            super(String.format(message + "- request info => %s", object));
        }
    }
}

