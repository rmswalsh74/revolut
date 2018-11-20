package com.revolut.resources;

import com.revolut.executor.Result;
import com.revolut.persistence.exception.CommandException;

import javax.ws.rs.core.Response;
import java.util.UUID;

public class BaseResource {
    protected Response response(Result result, Object entity) {
        if (result.wasSuccessful()) {
            if (result.getEntity() != null)
                return Response.ok().entity(result.getEntity()).build();
            else
                return Response.ok().entity(entity).build();
        } else if (result.getFailure() instanceof CommandException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.getFailure().getMessage()).build();
        } else {//BusinessException
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getFailure().getMessage()).build();
        }
    }

    protected Response response(Result result) {
        if (result.wasSuccessful()) {
            if (result.getEntity()!=null)
                return Response.ok().entity(result.getEntity()).build();
            else
                return Response.ok().build();
        } else if (result.getFailure() instanceof CommandException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.getFailure().getMessage()).build();
        } else {//BusinessException
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getFailure().getMessage()).build();
        }
    }

    protected Response response(Result result, UUID uuid) {
        if (result.wasSuccessful()) {
            return Response.ok(result).entity(uuid).build();
        } else if (result.getFailure() instanceof CommandException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.getFailure().getMessage()).build();
        } else {//BusinessException
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getFailure().getMessage()).build();
        }
    }
}

