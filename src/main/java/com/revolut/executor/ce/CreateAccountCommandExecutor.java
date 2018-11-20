package com.revolut.executor.ce;

import com.revolut.domain.Account;
import com.revolut.event.command.CommandEvent;
import com.revolut.event.command.CreateAccountCommandEvent;
import com.revolut.persistence.exception.BusinessException;
import com.revolut.persistence.exception.CommandException;
import com.revolut.persistence.service.AccountService;
import io.dropwizard.hibernate.UnitOfWork;

public class CreateAccountCommandExecutor extends CommandEventExecutor {

    private AccountService accountService;

    public CreateAccountCommandExecutor(AccountService accountService) {
        this.accountService=accountService;
    }

    @Override
    @UnitOfWork
    public void execute(CommandEvent cmd) throws BusinessException, CommandException {
        validate(cmd, CommandEvent.CREATE_ACCOUNT_COMMAND_EVENT);
        CreateAccountCommandEvent commandEvent=(CreateAccountCommandEvent)cmd;
        Account persisted=accountService.create(commandEvent.getAccount());
        commandEvent.getAccount().setId(persisted.getId());
    }
}
