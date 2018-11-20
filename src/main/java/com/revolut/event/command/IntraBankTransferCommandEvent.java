package com.revolut.event.command;

import com.revolut.domain.Transaction;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class IntraBankTransferCommandEvent extends CommandEvent {

    public static final String COMMAND_TYPE=INTRABANKTRANSFER;
    private Transaction transaction;

    public IntraBankTransferCommandEvent(Transaction transaction) {
        this.transaction=transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public String getType() {
        return COMMAND_TYPE;
    }
}
