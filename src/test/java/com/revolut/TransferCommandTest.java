package com.revolut;

import com.revolut.domain.*;
import com.revolut.event.command.CreateAccountCommandEvent;
import com.revolut.event.command.CreateBankCommandEvent;
import com.revolut.event.command.CreateUserCommandEvent;
import com.revolut.event.command.IntraBankTransferCommandEvent;
import com.revolut.executor.Invoker;
import com.revolut.executor.Result;
import com.revolut.executor.ce.CreateAccountCommandExecutor;
import com.revolut.executor.ce.CreateBankCommandExecutor;
import com.revolut.executor.ce.CreateUserCommandExecutor;
import com.revolut.executor.ce.IntraBankTransferCommandExecutor;
import com.revolut.persistence.dao.AccountDAO;
import com.revolut.persistence.dao.BankDAO;
import com.revolut.persistence.dao.TransactionDAO;
import com.revolut.persistence.dao.UserDAO;
import com.revolut.persistence.exception.AccountException;
import com.revolut.persistence.exception.BusinessException;
import com.revolut.persistence.service.AccountService;
import com.revolut.persistence.service.BankService;
import com.revolut.persistence.service.TransactionService;
import com.revolut.persistence.service.UserService;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DAOTestRule;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;

import java.util.Date;
import java.util.Optional;

import static org.fest.assertions.Assertions.*;

public class TransferCommandTest extends IntegrationTest {
    private static String baseUrl;
    private static Client client;

    @ClassRule
    public static final DropwizardAppRule<TransferConfiguration> RULE = new DropwizardAppRule<>(
            TransferApplication.class, CONFIG_PATH, ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

    @Rule
    public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
            .addEntityClass(Account.class)
            .addEntityClass(User.class)
            .addEntityClass(Bank.class)
            .addEntityClass(Transaction.class)
            .build();

    private AccountService accountService;
    private BankService bankService;
    private UserService userService;
    private TransactionService transactionService;
    private CreateBankCommandExecutor bankCommandExecutor;
    private CreateAccountCommandExecutor accountCommandExecutor;
    private CreateUserCommandExecutor userCommandExecutor;
    private IntraBankTransferCommandExecutor transferCommandExecutor;

    @Before
    public void setUp() {
        BankDAO bankDAO=new BankDAO(daoTestRule.getSessionFactory());
        UserDAO userDAO=new UserDAO(daoTestRule.getSessionFactory());
        AccountDAO accountDAO=new AccountDAO(daoTestRule.getSessionFactory());
        TransactionDAO transactionDAO=new TransactionDAO(daoTestRule.getSessionFactory());

        bankService=new BankService(bankDAO);
        accountService=new AccountService(accountDAO);
        userService=new UserService(userDAO);
        transactionService=new TransactionService(transactionDAO);

        bankCommandExecutor=new CreateBankCommandExecutor(bankService);
        accountCommandExecutor=new CreateAccountCommandExecutor(accountService);
        userCommandExecutor=new CreateUserCommandExecutor(userService);
        transferCommandExecutor=new IntraBankTransferCommandExecutor(daoTestRule.getSessionFactory(), transactionService, accountService);
    }

    //@Test
    public void testCreateBankAndTransferFunds() {

        Bank bank=DomainHelper.bank("Revolut");
        CreateBankCommandEvent bankCommandEvent=DomainHelper.bankCommandEvent(bank);
        Invoker invokerB=new Invoker(bankCommandExecutor, bankCommandEvent);
        Result resultB=invokerB.invokeCommand();
        assertThat(resultB.wasSuccessful()).isEqualTo(true);

        User user=DomainHelper.user(bank, "Richie");
        CreateUserCommandEvent userCommandEvent=DomainHelper.userCommandEvent(user);
        Invoker invokerU=new Invoker(userCommandExecutor, userCommandEvent);
        Result resultU=invokerU.invokeCommand();
        assertThat(resultU.wasSuccessful()).isEqualTo(true);;

        Account account1=DomainHelper.account(bank, user, 1234, "30-30-30");
        account1.setBalance(100d);
        CreateAccountCommandEvent accountCommandEvent1=DomainHelper.accountCommandEvent(account1);
        Invoker invokerA1=new Invoker(accountCommandExecutor, accountCommandEvent1);
        Result resultA1=invokerA1.invokeCommand();
        assertThat(resultA1.wasSuccessful()).isEqualTo(true);;

        Account account2=DomainHelper.account(bank, user, 1235, "40-40-40");
        CreateAccountCommandEvent accountCommandEvent2=DomainHelper.accountCommandEvent(account2);
        Invoker invokerA2=new Invoker(accountCommandExecutor, accountCommandEvent2);
        Result resultA2=invokerA2.invokeCommand();
        assertThat(resultA2.wasSuccessful()).isEqualTo(true);;

    }

    public void testTransfer(){
//        IntraBankTransferCommandEvent transferCommandEvent=new IntraBankTransferCommandEvent(DomainHelper.transaction(account1, account2, 50));
//        Invoker transfer=new Invoker(transferCommandExecutor, transferCommandEvent);
//        Result transferResult=transfer.invokeCommand();
//        assertThat(transferResult.wasSuccessful()).isEqualTo(true);
//
//        try {
//            Transaction transaction=transactionService.findByUuid(transferCommandEvent.getTransaction().getUuid());
//            assertThat(transaction.getUuid()).isEqualTo(transferCommandEvent.getTransaction().getUuid());
//            assertThat(transaction.getType()).isEqualTo(TransactionType.TRANSFER);
//            assertThat(transaction.getAmount()).isEqualTo(transferCommandEvent.getTransaction().getAmount());
//            assertThat(transaction.getFrom()).isEqualTo(account1);
//            assertThat(transaction.getTo()).isEqualTo(account2);
//            assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
//            Account from = accountService.findByAccountNumber(account1.getAccountNumber());
//            assertThat(from.getBalance()).isEqualTo(50d);
//            Account to = accountService.findByAccountNumber(account2.getAccountNumber());
//            assertThat(to.getBalance()).isEqualTo(50d);
//        } catch (BusinessException e) {
//            Assertions.fail(e.getMessage());
//        }
    }

}
