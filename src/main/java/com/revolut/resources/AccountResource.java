package com.revolut.resources;

import com.revolut.domain.Account;
import com.revolut.domain.User;
import com.revolut.event.command.CreateAccountCommandEvent;
import com.revolut.executor.Result;
import com.revolut.executor.ce.CommandEventExecutor;
import com.revolut.executor.ce.CreateAccountCommandExecutor;
import com.revolut.executor.Invoker;
import com.revolut.persistence.exception.AccountException;
import com.revolut.persistence.exception.BankException;
import com.revolut.persistence.exception.UserException;
import com.revolut.persistence.service.AccountService;
import com.revolut.persistence.service.UserService;
import com.revolut.resources.dto.AccountDTO;
import com.revolut.resources.transform.Transformer;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static com.revolut.resources.transform.Transformer.*;

@Path("/account")
public class AccountResource extends BaseResource {
    private static final String MESSAGE = "Hello, world!";

    private UserService userService;
    private AccountService accountService;
    private CommandEventExecutor executor;

    public AccountResource(AccountService  accountService, UserService userService, CreateAccountCommandExecutor executor) {
        this.accountService=accountService;
        this.userService=userService;
        this.executor=executor;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{accountNumber}")
    @UnitOfWork
    public Response get(@PathParam("accountNumber") Integer accountNumber) {
        try {
            Account account = accountService.findByAccountNumber(accountNumber);
            return Response.ok(account).build();
        } catch (AccountException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getErrorCode()).build();
        }
    }

    @POST
    @Produces(APPLICATION_JSON)
    @UnitOfWork
    public Response create(AccountDTO accountDTO) {
        try {
            User user=userService.findByName(accountDTO.getOwner());
            Account account=toEntity(accountDTO, user);
            Invoker invoker=new Invoker(executor, new CreateAccountCommandEvent(account));
            return response(invoker.invokeCommand());
        } catch (UserException e) {
            return response(new Result(e));
        }
    }
}
