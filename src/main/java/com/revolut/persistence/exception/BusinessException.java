package com.revolut.persistence.exception;

import java.util.Optional;

public class BusinessException extends Exception {

    private String message;
    private String errorCode;
    private Optional<Exception> exception;

    public BusinessException(String errorCode, String message, Optional<Exception> e) {
        this.errorCode=errorCode;
        this.message=message;
        this.exception=e;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Optional<Exception> getException() {
        return exception;
    }
}
