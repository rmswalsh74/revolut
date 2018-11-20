package com.revolut.executor.ce;

import com.revolut.domain.Bank;
import com.revolut.event.command.CommandEvent;
import com.revolut.event.command.CreateBankCommandEvent;
import com.revolut.persistence.exception.BusinessException;
import com.revolut.persistence.exception.CommandException;
import com.revolut.persistence.service.BankService;
import io.dropwizard.hibernate.UnitOfWork;

public class CreateBankCommandExecutor extends CommandEventExecutor {
    private BankService bankService;

    public CreateBankCommandExecutor(BankService bankService) {
        this.bankService=bankService;
    }

    @Override
    @UnitOfWork
    public void execute(CommandEvent cmd) throws BusinessException, CommandException {
        validate(cmd, CommandEvent.CREATE_BANK_COMMAND_EVENT);
        CreateBankCommandEvent commandEvent=(CreateBankCommandEvent)cmd;
        Bank persisted=bankService.create(commandEvent.getBank());
        commandEvent.getBank().setId(persisted.getId());
    }
}
