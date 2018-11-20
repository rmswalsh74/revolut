package com.revolut;

import com.revolut.domain.Account;
import com.revolut.domain.Bank;
import com.revolut.domain.Transaction;
import com.revolut.domain.User;
import com.revolut.executor.ce.CreateAccountCommandExecutor;
import com.revolut.executor.ce.CreateBankCommandExecutor;
import com.revolut.executor.ce.CreateUserCommandExecutor;
import com.revolut.executor.ce.IntraBankTransferCommandExecutor;
import com.revolut.persistence.dao.AccountDAO;
import com.revolut.persistence.dao.BankDAO;
import com.revolut.persistence.dao.TransactionDAO;
import com.revolut.persistence.dao.UserDAO;
import com.revolut.persistence.service.AccountService;
import com.revolut.persistence.service.BankService;
import com.revolut.persistence.service.TransactionService;
import com.revolut.persistence.service.UserService;
import com.revolut.resources.AccountResource;
import com.revolut.resources.BankResource;
import com.revolut.resources.TransferResource;
import com.revolut.resources.UserResource;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;

public class TransferApplication extends io.dropwizard.Application<TransferConfiguration> {
    public static void main(String[] args) throws Exception {
        new TransferApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<TransferConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    private final HibernateBundle<TransferConfiguration> hibernate =
            new HibernateBundle<TransferConfiguration>(Account.class, Bank.class, Transaction.class, User.class)
    {
        @Override
        public DataSourceFactory getDataSourceFactory(TransferConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public void run(TransferConfiguration config, Environment environment) throws Exception {
        TransactionService transactionService=new TransactionService(new TransactionDAO(hibernate.getSessionFactory()));
        AccountService accountService=new AccountService(new AccountDAO(hibernate.getSessionFactory()));
        IntraBankTransferCommandExecutor transferCommandExecutor=new IntraBankTransferCommandExecutor(hibernate.getSessionFactory(), transactionService, accountService);
        ConsumerExecutionService consumerExecutionService=new ConsumerExecutionService(transferCommandExecutor);
        environment.lifecycle().manage(consumerExecutionService);
        environment.getObjectMapper().setPropertyNamingStrategy(SNAKE_CASE);
        BankService bankService=new BankService(new BankDAO(hibernate.getSessionFactory()));
        UserService userService=new UserService(new UserDAO(hibernate.getSessionFactory()));

        environment.jersey().register(
                new TransferResource(accountService, transactionService, transferCommandExecutor));
        environment.jersey().register(
                new BankResource(bankService,
                        new CreateBankCommandExecutor(bankService)));
        environment.jersey().register(
                new UserResource(userService, bankService,
                        new CreateUserCommandExecutor(userService)));
        environment.jersey().register(
                new AccountResource(accountService, userService,
                        new CreateAccountCommandExecutor(accountService)));
    }
}
