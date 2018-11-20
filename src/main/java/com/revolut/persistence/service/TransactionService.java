package com.revolut.persistence.service;

import com.revolut.domain.Account;
import com.revolut.domain.Transaction;
import com.revolut.persistence.dao.TransactionDAO;
import com.revolut.persistence.exception.AccountException;
import com.revolut.persistence.exception.BankException;
import com.revolut.persistence.exception.TransactionException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionService {
    public TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO=transactionDAO;
    }

    public Transaction findByUuid(UUID uuid) throws TransactionException {
        if (uuid==null)
            throw new TransactionException(TransactionException.TANSACTION_UUID_CANNOT_BE_NULL,
                String.format("Transaction.findByUuid: UUID cannot be null"));
        Transaction transaction=transactionDAO.findByUuid(uuid)
                .orElseThrow(() ->
                        new TransactionException(TransactionException.TRANSACTION_DOES_NOT_EXIST,
                                String.format("Transaction %s does not exist", uuid)));
        return transaction;
    }

    public List<Transaction> findAll() {
        return transactionDAO.findAll();
    }

    public Transaction create(Transaction transaction) throws TransactionException {
        try {
            if (transaction==null)
                throw new TransactionException(TransactionException.TANSACTION_CANNOT_BE_NULL,
                        String.format("Transaction.create: Transaction cannot be null"));
            //should never be duplicates
            return transactionDAO.create(transaction);
        } catch (ConstraintViolationException e) {
            throw new TransactionException(TransactionException.TRANSACTION_ALREADY_EXISTS,
                    String.format("Transaction %s already exists", transaction));
        }
    }

    public Transaction update(Transaction transaction) throws TransactionException {
        if (transaction==null)
            throw new TransactionException(TransactionException.TANSACTION_CANNOT_BE_NULL,
                    String.format("Transaction.update: Transaction cannot be null"));
        Optional<Transaction> existing=transactionDAO.findById(transaction.getId());
        if (!existing.isPresent()) {
            throw new TransactionException(TransactionException.TRANSACTION_DOES_NOT_EXIST,
                    String.format("Transaction %s does not exist", existing));
        }
        return transactionDAO.update(transaction);
    }
}
