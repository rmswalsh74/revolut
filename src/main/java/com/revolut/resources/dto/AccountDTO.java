package com.revolut.resources.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Currency;

public class AccountDTO {
    String owner;
    @JsonProperty(value = "sortCode")
    String sortCode;
    @JsonProperty(value = "accountNumber")
    Integer accountNumber;
    @JsonProperty(value = "bankAccountName")
    String bankAccountName;
    @JsonProperty(value = "bankName")
    String bankName;
    double balance;
    Currency currency;

    public AccountDTO(){}

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
