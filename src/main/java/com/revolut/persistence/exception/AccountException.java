package com.revolut.persistence.exception;

public class AccountException extends BusinessException {

    public static final String ACCOUNT_DOES_NOT_EXIST="ACC_0001";
    public static final String ACCOUNT_CLOSED="ACC_0002";
    public static final String ACCOUNT_FUNDS_UNAVAILABLE="ACC_0003";
    public static final String ACCOUNT_ALREADY_EXISTS="ACC_0004";
    public static final String ACCOUNT_NUMBER_CANNOT_BE_NULL="ACC_0005";
    public static final String ACCOUNT_CANNOT_BE_NULL="ACC_0006";

    public AccountException(String errorCode, String message) {
        super(errorCode, message, null);
    }
}
