package com.revolut.persistence.exception;

public class UserException extends BusinessException {

    public static final String USER_DOES_NOT_EXIST="USER_0001";
    public static final String USER_ALREADY_EXISTS="USER_0002";
    public static final String USER_NAME_CANNOT_BE_NULL="USER_0003";
    public static final String USER_CANNOT_BE_NULL="USER_0004";

    public UserException(String errorCode, String message) {
        super(errorCode, message, null);
    }
}
