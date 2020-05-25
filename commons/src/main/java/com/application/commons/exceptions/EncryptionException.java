package com.application.commons.exceptions;


public class EncryptionException extends RuntimeException {

    private static final long serialVersionUID = 20617294432397286L;

    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(Throwable cause) {
        super(cause);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
