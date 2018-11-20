package com.revolut.persistence.dao;

import com.revolut.domain.Account;
import com.revolut.domain.Transaction;
import com.revolut.persistence.exception.TransactionException;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionDAO extends AbstractDAO<Transaction> {
    public TransactionDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Transaction> findById(Integer id) {
        return Optional.ofNullable(get(id));
    }

    public Optional<Transaction> findByUuid(UUID uuid) {
        return  Optional.ofNullable(uniqueResult(
                namedQuery("com.revolut.domain.Transaction.findByUuid")
                        .setParameter("uuid", uuid)));
    }

    /**
     * Set the transaction to the Hash of the object
     * If there happens to be duplicate transactions there will be a clash at the
     * database level.
     * @param transaction
     * @return
     */
    public Transaction create(Transaction transaction) {
        transaction.setId(transaction.hashCode());
        return persist(transaction);
    }

    public Transaction update(Transaction transaction) {
        return persist(transaction);
    }

    @SuppressWarnings("unchecked")
    public List<Transaction> findAll() {
        return list((Query<Transaction>) namedQuery("com.revolut.domain.Transaction.findAll"));
    }
}
