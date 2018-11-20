package com.revolut.event.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.revolut.domain.Account;
import com.revolut.domain.Bank;

public class CreateBankCommandEvent extends CommandEvent {
    private Bank bank;

    /**
     * Default Constructor for Jackson
     */
    public CreateBankCommandEvent(){}

    public CreateBankCommandEvent(Bank bank) {
        this.bank=bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }

    @JsonIgnore
    @Override
    public String getType() {
        return CREATE_BANK_COMMAND_EVENT;
    }
}
