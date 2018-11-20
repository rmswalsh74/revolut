package com.revolut.persistence.exception;

public class TransactionException extends BusinessException {
    public static final String TRANSACTION_DOES_NOT_EXIST="TRANS_0001";
    public static final String TRANSACTION_ALREADY_EXISTS="TRANS_0002";
    public static final String TANSACTION_UUID_CANNOT_BE_NULL="TRANS_0003";
    public static final String TANSACTION_CANNOT_BE_NULL="TRANS_0004";

    public TransactionException(String errorCode, String message) {
        super(errorCode, message, null);
    }
}
