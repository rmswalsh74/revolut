package com.revolut.persistence.service;

import com.revolut.domain.Account;
import com.revolut.persistence.exception.AccountException;
import com.revolut.persistence.dao.AccountDAO;
import com.revolut.persistence.exception.BankException;

import java.util.Optional;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO=accountDAO;
    }

    public Account findByAccountNumber(Integer accountNumber) throws AccountException {
        if (accountNumber==null)
            throw new AccountException(AccountException.ACCOUNT_NUMBER_CANNOT_BE_NULL,
                    String.format("Account.findByAccountNumber: Account Number cannot be null"));
        Account account=accountDAO.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountException(AccountException.ACCOUNT_DOES_NOT_EXIST,
                                String.format("Account %d does not exist", accountNumber)));
        if (account.isClosed()) {
            new AccountException(AccountException.ACCOUNT_CLOSED,
                    String.format("Account %d is closed", accountNumber));
        }
        return account;
    }

    public Account create(Account account) throws AccountException {
        if (account==null)
            throw new AccountException(AccountException.ACCOUNT_CANNOT_BE_NULL,
                    String.format("Account.create: Account cannot be null"));

        Optional<Account> existing=accountDAO.findByAccountNumber(account.getAccountNumber());
        if (existing.isPresent()) {
            throw new AccountException(AccountException.ACCOUNT_ALREADY_EXISTS,
                    String.format("Account with accountNumber %d already exists", account.getAccountNumber()));
        }
        return accountDAO.persist(account);
    }

    public Account update(Account account) throws AccountException {
        if (account==null)
            throw new AccountException(AccountException.ACCOUNT_CANNOT_BE_NULL,
                    String.format("Account.update: Account cannot be null"));

        Optional<Account> existing=accountDAO.findByAccountNumber(account.getAccountNumber());
        if (!existing.isPresent()) {
            throw new AccountException(AccountException.ACCOUNT_DOES_NOT_EXIST,
                    String.format("Account with accountNumber %d does not exist", account.getAccountNumber()));
        }
        return accountDAO.persist(account);
    }
}
