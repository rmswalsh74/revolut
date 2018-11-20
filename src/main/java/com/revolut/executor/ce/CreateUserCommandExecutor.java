package com.revolut.executor.ce;

import com.revolut.domain.User;
import com.revolut.event.command.CommandEvent;
import com.revolut.event.command.CreateBankCommandEvent;
import com.revolut.event.command.CreateUserCommandEvent;
import com.revolut.persistence.exception.BusinessException;
import com.revolut.persistence.exception.CommandException;
import com.revolut.persistence.service.BankService;
import com.revolut.persistence.service.UserService;
import io.dropwizard.hibernate.UnitOfWork;

public class CreateUserCommandExecutor extends CommandEventExecutor {
    private UserService userService;

    public CreateUserCommandExecutor(UserService userService) {
        this.userService=userService;
    }

    @Override
    @UnitOfWork
    public void execute(CommandEvent cmd) throws BusinessException, CommandException {
        validate(cmd, CommandEvent.CREATE_USER_COMMAND_EVENT);
        CreateUserCommandEvent commandEvent=(CreateUserCommandEvent)cmd;
        User persisted=userService.create(commandEvent.getUser());
        commandEvent.getUser().setId(persisted.getId());
    }}
