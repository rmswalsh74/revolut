package com.revolut.resources;

import com.revolut.domain.Bank;
import com.revolut.event.command.CreateBankCommandEvent;
import com.revolut.executor.Invoker;
import com.revolut.executor.ce.CommandEventExecutor;
import com.revolut.executor.ce.CreateBankCommandExecutor;
import com.revolut.persistence.exception.BankException;
import com.revolut.persistence.service.BankService;
import com.revolut.resources.dto.BankDTO;
import com.revolut.resources.transform.Transformer;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/bank")
public class BankResource extends BaseResource {
    private BankService bankService;
    private CommandEventExecutor executor;

    public BankResource(BankService  bankService, CreateBankCommandExecutor executor) {
        this.bankService=bankService;
        this.executor=executor;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{bankName}")
    @UnitOfWork
    public Response get(@PathParam("bankName") String name) {
        try {
            Bank bank = bankService.findByName(name);
            return Response.ok(bank).build();
        } catch (BankException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getErrorCode()).build();
        }
    }

    @POST
    @Produces(APPLICATION_JSON)
    @UnitOfWork
    public Response create(BankDTO bankDTO) {
        Bank bank= Transformer.toEntity(bankDTO);
        Invoker invoker=new Invoker(executor, new CreateBankCommandEvent(bank));
        return response(invoker.invokeCommand());
    }
}
