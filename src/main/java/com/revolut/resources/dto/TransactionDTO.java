package com.revolut.resources.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.revolut.domain.TransactionType;

import java.util.Date;

public class TransactionDTO {
    @JsonProperty(value = "from")
    Integer fromAccountNumber;
    @JsonProperty(value = "to")
    Integer toAccountNumber;
    double amount;
    TransactionType type;
    Date date;

    public TransactionDTO(){}

    public Integer getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(Integer fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public Integer getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(Integer toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
