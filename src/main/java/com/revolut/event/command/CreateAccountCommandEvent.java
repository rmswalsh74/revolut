package com.revolut.event.command;

import com.revolut.domain.Account;

public class CreateAccountCommandEvent extends CommandEvent {
    private Account account;

    /**
     * Default Constructor for Jackson
     */
    public CreateAccountCommandEvent(){}

    public CreateAccountCommandEvent(Account account) {
        this.account=account;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String getType() {
        return CREATE_ACCOUNT_COMMAND_EVENT;
    }
}
