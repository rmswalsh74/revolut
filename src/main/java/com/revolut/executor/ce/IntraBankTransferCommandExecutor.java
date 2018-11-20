package com.revolut.executor.ce;

import com.revolut.domain.*;
import com.revolut.domain.Transaction;
import com.revolut.event.command.CommandEvent;
import com.revolut.event.command.IntraBankTransferCommandEvent;
import com.revolut.persistence.exception.AccountException;
import com.revolut.persistence.exception.BusinessException;
import com.revolut.persistence.exception.CommandException;
import com.revolut.persistence.service.AccountService;
import com.revolut.persistence.service.TransactionService;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.*;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.revolut.event.command.CommandEvent.INTRABANKTRANSFER;

public class IntraBankTransferCommandExecutor extends CommandEventExecutor {

    private TransactionService transactionService;
    private AccountService accountService;
    private SessionFactory sessionFactory;

    public IntraBankTransferCommandExecutor(SessionFactory sessionFactory, TransactionService transactionService, AccountService accountService) {
        super(sessionFactory);
        this.transactionService=transactionService;
        this.accountService=accountService;
        this.sessionFactory=sessionFactory;
    }

    /**
     * Executed within a Transaction
     *
     * TODO: Update to work for many executors being executed at once.
     *
     * 1. Validates if the event is correct
     * 2. Validates of the accounts exists
     * 3. Transfers the money if the funds are available
     *
     * @param event
     * @throws BusinessException If The Accounts do not exist
     * @throws CommandException If the event is not to be executed here
     */
    @Override
    @UnitOfWork
    public void execute(CommandEvent event) throws BusinessException, CommandException {
        try {
            validate(event, INTRABANKTRANSFER);
            IntraBankTransferCommandEvent transferCommandEvent=(IntraBankTransferCommandEvent)event;
            Transaction transaction=transferCommandEvent.getTransaction();
            transactionService.create(transaction);
            //Load Account Objects so as to associate them with the session
            Account fromAccount=accountService.findByAccountNumber(transaction.getFrom().getAccountNumber());
            Account toAccount=accountService.findByAccountNumber(transaction.getTo().getAccountNumber());
            if (toAccount.isClosed()) {
                //No point in going on here
                String message=String.format("The forwarding account was closed %d", toAccount.getAccountNumber());
                transaction.setStatus(TransactionStatus.NOT_PROCESSED_TO_ACCOUNT_WAS_CLOSED);
                transaction.setMessage(message);
                transaction.setDateCompleted(new Date());
                transactionService.update(transaction);
            } else if (fromAccount.debit(transaction.getAmount())== FundState.FUNDS_AVAILABLE) {
                AccountState toAccountState=toAccount.credit(transaction.getAmount());
                transaction.setStatus(TransactionStatus.COMPLETED);
                transaction.setDateCompleted(new Date());
                accountService.update(fromAccount);
                accountService.update(toAccount);
                transactionService.update(transaction);
            } else {
                String message=String.format("No funds on account for %d", fromAccount.getAccountNumber());
                transaction.setStatus(TransactionStatus.NOT_PROCESSED_NOT_ENOUGH_FUNDS);
                transaction.setMessage(message);
                transaction.setDateCompleted(new Date());
                transactionService.update(transaction);
            }
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }
}
