package com.revolut.persistence.dao;

import com.revolut.domain.Account;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class AccountDAO extends AbstractDAO<Account> {
    public AccountDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Optional<Account> findByAccountNumber(Integer accountNumber) {
        return  Optional.ofNullable(uniqueResult(
                namedQuery("com.revolut.domain.Account.findByAccountNumber")
                        .setParameter("accountNumber", accountNumber)));
    }

    public Account persist(Account account) {
        return super.persist(account);
    }

    public Account update(Account account) {
        return update(account);
    }

    @SuppressWarnings("unchecked")
    public List<Account> findAll() {
        return list((Query<Account>) namedQuery("com.revolut.domain.Account.findAll"));
    }
}
