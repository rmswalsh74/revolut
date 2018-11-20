package com.revolut.executor.ce;

import com.revolut.event.command.CommandEvent;
import com.revolut.persistence.exception.BusinessException;
import com.revolut.persistence.exception.CommandException;
import org.hibernate.*;
import org.hibernate.context.internal.ManagedSessionContext;

public abstract class CommandEventExecutor {

    private SessionFactory sessionFactory;
    protected CommandEventExecutor(){}

    protected CommandEventExecutor(SessionFactory sessionFactory){
        this.sessionFactory=sessionFactory;
    }

    public void validate(CommandEvent command, String expected) throws CommandException {
        if (!command.getType().equals(expected)) {
            throw new CommandException(CommandException.EXECUTOR_CANNOT_EXECUTE_COMMAND,
                    String.format("RuntimeException: CommandExecutor Expected: $s Received: %s",
                            expected, command.getType()));
        }
    }

    public abstract void execute(CommandEvent command) throws BusinessException;

    public void executeUnmanaged(CommandEvent command) throws BusinessException{
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.setDefaultReadOnly(true);
            session.setCacheMode(CacheMode.NORMAL);
            session.setFlushMode(FlushMode.MANUAL);
            ManagedSessionContext.bind(session);
            execute(command);
            session.flush();
        } finally {
            tx.commit();
            session.close();
        }
    }
}
