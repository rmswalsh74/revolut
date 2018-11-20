package com.revolut.resources;

import com.revolut.domain.Bank;
import com.revolut.domain.User;
import com.revolut.event.command.CreateUserCommandEvent;
import com.revolut.executor.Invoker;
import com.revolut.executor.Result;
import com.revolut.executor.ce.CommandEventExecutor;
import com.revolut.executor.ce.CreateUserCommandExecutor;
import com.revolut.persistence.exception.BankException;
import com.revolut.persistence.exception.UserException;
import com.revolut.persistence.service.BankService;
import com.revolut.persistence.service.UserService;
import com.revolut.resources.dto.UserDTO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static com.revolut.resources.transform.Transformer.*;

@Path("/user")
public class UserResource extends BaseResource {
    private static final String MESSAGE = "Hello, world!";

    private UserService userService;
    private BankService bankService;
    private CommandEventExecutor executor;

    public UserResource(UserService userService, BankService bankService, CreateUserCommandExecutor executor) {
        this.userService=userService;
        this.bankService=bankService;
        this.executor=executor;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{userName}")
    @UnitOfWork
    public Response get(@PathParam("userName") String name) {
        try {
            User user = userService.findByName(name);
            return Response.ok(user).build();
        } catch (UserException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getErrorCode()).build();
        }
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @UnitOfWork
    public Response create(UserDTO userDTO) {
        try {
            Bank bank=bankService.findByName(userDTO.getBankName());
            User user=toEntity(userDTO, bank);
            Invoker invoker=new Invoker(executor, new CreateUserCommandEvent(user));
            return response(invoker.invokeCommand(), toDto(user));
        } catch (BankException e) {
            return response(new Result(e));
        }
    }
}
