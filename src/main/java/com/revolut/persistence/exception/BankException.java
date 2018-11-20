package com.revolut.persistence.exception;

public class BankException extends BusinessException {

    public static final String BANK_ALREADY_EXISTS="BANK_0001";
    public static final String BANK_DOES_NOT_EXIST="BANK_0002";
    public static final String BANK_NAME_CANNOT_BE_NULL="BANK_0003";
    public static final String BANK_CANNOT_BE_NULL="BANK_0004";

    public BankException(String errorCode, String message) {
        super(errorCode, message, null);
    }
}
