package com.revolut.resources;

import com.revolut.domain.Account;
import com.revolut.domain.Transaction;
import com.revolut.event.command.IntraBankTransferCommandEvent;
import com.revolut.executor.Result;
import com.revolut.executor.ce.CommandEventExecutor;
import com.revolut.executor.ce.IntraBankTransferCommandExecutor;
import com.revolut.executor.Invoker;
import com.revolut.integration.MessageBroker;
import com.revolut.persistence.exception.AccountException;
import com.revolut.persistence.exception.TransactionException;
import com.revolut.persistence.service.AccountService;
import com.revolut.persistence.service.TransactionService;
import com.revolut.resources.dto.TransactionDTO;
import io.dropwizard.hibernate.UnitOfWork;
import jersey.repackaged.com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.glassfish.jersey.process.JerseyProcessingUncaughtExceptionHandler;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static com.revolut.resources.transform.Transformer.*;

@Path("/transfer")
public class TransferResource extends BaseResource {
    private static final String MESSAGE = "Hello, world!";

    private AccountService accountService;
    private TransactionService transactionService;
    private CommandEventExecutor commandExecutor;

    public TransferResource(AccountService accountService, TransactionService transactionService, IntraBankTransferCommandExecutor intraBankTransferExecutor) {
        this.accountService=accountService;
        this.transactionService=transactionService;
        this.commandExecutor=intraBankTransferExecutor;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/findAll")
    @UnitOfWork
    public Response finaAll() {
        List<Transaction> transactions=transactionService.findAll();
        return Response.ok().entity(transactions).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{uuid}")
    @UnitOfWork
    public Response status(@PathParam("uuid") UUID uuid) {
        try {
            Transaction transaction=transactionService.findByUuid(uuid);
            return Response.ok().entity(transaction.getStatus()).build();
        } catch (TransactionException e) {
            return response(new Result(e));
        }
    }

    @POST
    @Produces(APPLICATION_JSON)
    @UnitOfWork
    public Response transferSynch(TransactionDTO transactionDTO) {
        try {
            IntraBankTransferCommandEvent transferCommandEvent=createTransferCommand(transactionDTO);
            Invoker invoker=new Invoker(commandExecutor, transferCommandEvent);
            return response(invoker.invokeCommand(), transferCommandEvent.getTransaction().getUuid());
        } catch (AccountException e) {
            return response(new Result(e));
        }
    }

    @Path("/async")
    @POST
    @Produces(APPLICATION_JSON)
    @UnitOfWork
    public Response transferAsync(TransactionDTO transferDTO) {
        try {
            //Generate the UUUID here so that we can track this transaction via the status API
            IntraBankTransferCommandEvent transferCommandEvent=createTransferCommand(transferDTO);
            if (MessageBroker.getInstance().publish(transferCommandEvent)) {
                return Response.ok().entity(transferCommandEvent.getTransaction().getUuid()).build();
            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }
        } catch (AccountException e) {
            return response(new Result(e));
        }
    }

    private IntraBankTransferCommandEvent createTransferCommand(TransactionDTO transactionDTO) throws AccountException {
        Account fromAccount=accountService.findByAccountNumber(transactionDTO.getFromAccountNumber());
        Account toAccount=accountService.findByAccountNumber(transactionDTO.getToAccountNumber());
        Transaction transaction=toEntity(transactionDTO, fromAccount, toAccount);
        IntraBankTransferCommandEvent transferCommandEvent=new IntraBankTransferCommandEvent(transaction);
        return transferCommandEvent;
    }
}
