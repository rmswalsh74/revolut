package com.revolut.persistence.dao;

import com.revolut.domain.Account;
import com.revolut.domain.Bank;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class BankDAO extends AbstractDAO<Bank> {
    public BankDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Bank> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Bank create(Bank bank) {
        return persist(bank);
    }

    public Bank update(Bank bank) {
        return update(bank);
    }

    @SuppressWarnings("unchecked")
    public List<Bank> findAll() {
        return list((Query<Bank>) namedQuery("com.revolut.domain.Bank.findAll"));
    }

    public Optional<Bank> findByName(String name) {
        return  Optional.ofNullable(uniqueResult(
                namedQuery("com.revolut.domain.Bank.findByName")
                        .setParameter("name", name)));
    }
}
