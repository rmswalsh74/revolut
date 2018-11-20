package com.revolut.domain;

public enum  TransactionStatus {
    ACCEPTED,
    PROCESSING,
    COMPLETED,
    FAILED,
    NOT_PROCESSED_TO_ACCOUNT_WAS_CLOSED,
    NOT_PROCESSED_NOT_ENOUGH_FUNDS;
}
