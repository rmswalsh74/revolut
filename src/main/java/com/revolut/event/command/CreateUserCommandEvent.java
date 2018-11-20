package com.revolut.event.command;

import com.revolut.domain.Bank;
import com.revolut.domain.User;

import static com.revolut.event.command.CommandEvent.CREATE_USER_COMMAND_EVENT;

public class CreateUserCommandEvent extends CommandEvent {
    private User user;

    /**
     * Default Constructor for Jackson
     */
    public CreateUserCommandEvent(){}

    public CreateUserCommandEvent(User user) {
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getType() {
        return CREATE_USER_COMMAND_EVENT;
    }
}
