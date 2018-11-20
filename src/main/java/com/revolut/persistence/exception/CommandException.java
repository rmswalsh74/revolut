package com.revolut.persistence.exception;

import java.util.Optional;

public class CommandException extends RuntimeException {

    public static Integer EXECUTOR_CANNOT_EXECUTE_COMMAND=1001;

    private Integer errorCode;
    private String message;
    private Optional<Exception> exception;
    public CommandException(Integer errorCode, String message) {
        this.errorCode=errorCode;
        this.message=message;
    }

    public CommandException(Integer errorCode, String message, Optional<Exception> e) {
        this.errorCode=errorCode;
        this.message=message;
        this.exception=e;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Optional<Exception> getException() {
        return exception;
    }
}
