package com.revolut.executor;

import com.revolut.event.command.CommandEvent;
import com.revolut.executor.ce.CommandEventExecutor;
import com.revolut.persistence.exception.BusinessException;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Invoker {

    private static final Logger log = LoggerFactory.getLogger(Invoker.class);
    //CommandEventExecutor to invoke
    private CommandEventExecutor commandExecutor;
    private CommandEvent command;

    public Invoker(CommandEventExecutor executor, CommandEvent command) {
        this.commandExecutor=executor;
        this.command=command;
    }

    public Result invokeCommandUnmanaged(){
        try {
            this.commandExecutor.executeUnmanaged(command);
            return new Result(true);
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
            return new Result(e);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            return new Result(e);
        }
    }

    public Result invokeCommand() {
        try {
            this.commandExecutor.execute(command);
            return new Result(true);
        } catch (BusinessException e) {
            log.error(e.getMessage(), e);
            return new Result(e);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            return new Result(e);
        }
    }
}
