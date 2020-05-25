package com.application.commons.exceptions;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 20617294432397286L;


    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
