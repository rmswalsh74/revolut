package com.revolut;

import com.revolut.domain.Account;
import com.revolut.domain.Bank;
import com.revolut.domain.Transaction;
import com.revolut.domain.User;
import com.revolut.persistence.exception.BankException;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.cfg.AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS;

public class TransferBankTest extends IntegrationTest {

    private static String baseUrl;
    private static Client client;

    @ClassRule
    public static final DropwizardAppRule<TransferConfiguration> RULE = new DropwizardAppRule<>(
            TransferApplication.class, CONFIG_PATH, ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));


    ManagedDataSource ds = RULE.getConfiguration().getDataSourceFactory().build(RULE.getEnvironment().metrics(), "test");

    @BeforeClass
    public static void setUp() {
        baseUrl = String.format("http://localhost:%d", RULE.getLocalPort());
        client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
    }

    @Test
    public void testCreateBank() {
        Response response = client.target(String.format("%s/bank", baseUrl)).request(APPLICATION_JSON_TYPE).post(
                Entity.entity(DomainHelper.bankDTO("Revolut"), MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatus()).isEqualTo(200);
    }

}
