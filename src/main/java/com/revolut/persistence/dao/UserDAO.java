package com.revolut.persistence.dao;

import com.revolut.domain.Account;
import com.revolut.domain.Bank;
import com.revolut.domain.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public User create(User user) {
        return persist(user);
    }

    public User update(User user) {
        return update(user);
    }

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return list((Query<User>) namedQuery("com.revolut.domain.User.findAll"));
    }

    public Optional<User> findByName(String name) {
        return  Optional.ofNullable(uniqueResult(
                namedQuery("com.revolut.domain.User.findByName")
                        .setParameter("name", name)));
    }
}
