package com.revolut.persistence.service;

import com.revolut.domain.Bank;
import com.revolut.domain.User;
import com.revolut.persistence.dao.BankDAO;
import com.revolut.persistence.dao.UserDAO;
import com.revolut.persistence.exception.AccountException;
import com.revolut.persistence.exception.BankException;
import com.revolut.persistence.exception.TransactionException;
import com.revolut.persistence.exception.UserException;
import org.w3c.dom.UserDataHandler;

import java.util.Optional;

public class UserService {
    public UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO=userDAO;
    }

    public User findByName(String name) throws UserException {
        if (name==null)
            throw new UserException(UserException.USER_NAME_CANNOT_BE_NULL,
                    String.format("User.findByName: Name cannot be null"));
        User user=userDAO.findByName(name)
                .orElseThrow(() ->
                        new UserException(UserException.USER_DOES_NOT_EXIST,
                                String.format("User %s does not exist", name)));
        return user;
    }

    public User create(User user) throws UserException {
        if (user==null)
            throw new UserException(UserException.USER_NAME_CANNOT_BE_NULL,
                    String.format("User.findByName: Name cannot be null"));
        Optional<User> existing=userDAO.findByName(user.getName());
        if (existing.isPresent()) {
            throw new UserException(UserException.USER_ALREADY_EXISTS,
                    String.format("User with name %s already exists", user.getName()));
        }
        return userDAO.create(user);
    }
}
