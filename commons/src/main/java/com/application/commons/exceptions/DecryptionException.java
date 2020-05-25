package com.application.commons.exceptions;


public class DecryptionException extends RuntimeException  {
    private static final long serialVersionUID = 20617294432397286L;


    public DecryptionException(String message) {
        super(message);
    }

    public DecryptionException(Throwable cause) {
        super(cause);
    }

    public DecryptionException(String message, Throwable cause) {
        super(message, cause);
    }

}
