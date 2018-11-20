package com.revolut.persistence.service;

import com.revolut.domain.Account;
import com.revolut.domain.Bank;
import com.revolut.persistence.exception.AccountException;
import com.revolut.persistence.dao.BankDAO;
import com.revolut.persistence.exception.BankException;

import java.util.Optional;

public class BankService {
    public BankDAO bankDAO;

    public BankService(BankDAO bankDAO) {
        this.bankDAO=bankDAO;
    }

    public Bank findByName(String name) throws BankException {
        if (name==null)
            throw new BankException(BankException.BANK_NAME_CANNOT_BE_NULL,
                    String.format("Bank.findByName: Bank name cannot be null"));
        Bank bank=bankDAO.findByName(name)
                .orElseThrow(() ->
                        new BankException(BankException.BANK_DOES_NOT_EXIST,
                                String.format("Bank %s does not exist", name)));
        return bank;
    }

    public Bank create(Bank bank) throws BankException {
        if (bank==null)
            throw new BankException(BankException.BANK_CANNOT_BE_NULL,
                    String.format("Bank.findByName: Bank name cannot be null"));
        Optional<Bank> existing=bankDAO.findByName(bank.getName());
        if (existing.isPresent()) {
            throw new BankException(AccountException.ACCOUNT_ALREADY_EXISTS,
                    String.format("Bank with name %s already exists", bank.getName()));
        }
        return bankDAO.create(bank);
    }
}
