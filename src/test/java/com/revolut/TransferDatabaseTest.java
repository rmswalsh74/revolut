package com.revolut;

import com.revolut.domain.Account;
import com.revolut.domain.Bank;
import com.revolut.domain.Transaction;
import com.revolut.domain.User;
import com.revolut.persistence.dao.AccountDAO;
import com.revolut.persistence.dao.BankDAO;
import com.revolut.persistence.dao.TransactionDAO;
import com.revolut.persistence.dao.UserDAO;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DAOTestRule;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.fest.assertions.Assert;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.Rule;
import org.junit.Before;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.Currency;
import java.util.Locale;

import static org.fest.assertions.Assertions.*;

public class TransferDatabaseTest extends IntegrationTest {

    @Rule
    public DAOTestRule daoTestRule = DAOTestRule.newBuilder()
            .addEntityClass(Account.class)
            .addEntityClass(User.class)
            .addEntityClass(Bank.class)
            .addEntityClass(Transaction.class)
            .build();

    private AccountDAO accountDAO;
    private UserDAO userDAO;
    private BankDAO bankDAO;
    private TransactionDAO transactionDAO;
    @Before
    public void setUp() {
        bankDAO=new BankDAO(daoTestRule.getSessionFactory());
        userDAO=new UserDAO(daoTestRule.getSessionFactory());
        accountDAO=new AccountDAO(daoTestRule.getSessionFactory());
        transactionDAO=new TransactionDAO(daoTestRule.getSessionFactory());
    }

    @Test
    public void createDomainObjectsHappyPath() {
        Bank bank=daoTestRule.inTransaction(() -> bankDAO.create(
                DomainHelper.bank("Revolut")
        ));
        User user=daoTestRule.inTransaction(() -> userDAO.create(
                DomainHelper.user(bank, "Richard Walsh")
        ));
        Account from=daoTestRule.inTransaction(() -> accountDAO.persist(
                DomainHelper.account(bank, user, 1, "30-30-30")
        ));
        Account to=daoTestRule.inTransaction(() -> accountDAO.persist(
                DomainHelper.account(bank, user, 2, "40-40-40")
        ));
        Transaction transaction=daoTestRule.inTransaction(()->
                DomainHelper.transaction(from, to, 100));
        assertThat(from.getId()).isGreaterThan(0);
        assertThat(from.getAccountName()).isEqualTo("Savings");
        assertThat(from.getSortCode()).isEqualTo("30-30-30");
    }

    @Test
    public void createDuplicateAccounts() {
        Account from= null;
        Account to= null;
        try {
            Bank bank=daoTestRule.inTransaction(() -> bankDAO.create(
                    DomainHelper.bank("Revolut")
            ));
            User user=daoTestRule.inTransaction(() -> userDAO.create(
                    DomainHelper.user(bank, "Richard Walsh")
            ));
            from = daoTestRule.inTransaction(() -> accountDAO.persist(
                    DomainHelper.account(bank, user, 1, "30-30-30")
            ));
            to = daoTestRule.inTransaction(() -> accountDAO.persist(
                    DomainHelper.account(bank, user, 2, "40-40-40")
            ));
            Account to2=daoTestRule.inTransaction(() -> accountDAO.persist(
                    DomainHelper.account(bank, user, 1, "50-50-50")
            ));
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(PersistenceException.class);
        }
    }

    @Test
    public void testInvalidSortCode() {
        Bank bank=daoTestRule.inTransaction(() -> bankDAO.create(
                DomainHelper.bank("Revolut")
        ));
        User user=daoTestRule.inTransaction(() -> userDAO.create(
                DomainHelper.user(bank, "Richard Walsh")
        ));
        try {
            Account from=daoTestRule.inTransaction(() -> accountDAO.persist(
                    DomainHelper.account(bank, user, 1, "304545")
            ));
        } catch (Exception e) {
            Assertions.assertThat(e).isInstanceOf(ConstraintViolationException.class);
        }

    }

    @Test
    public void createDuplicateBanks() {
        Bank bank=daoTestRule.inTransaction(() -> bankDAO.create(
                DomainHelper.bank("Revolut")
        ));
        Bank dup=daoTestRule.inTransaction(() -> bankDAO.create(
                DomainHelper.bank("Revolut")
        ));
//        assertThat(dup.getId()).isGreaterThan(0);
//        assertThat(dup.getAccountName()).isEqualTo("Savings");
//        assertThat(dup.getSortCode()).isEqualTo("30-30-30");
    }
}
