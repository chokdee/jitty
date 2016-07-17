package com.jmelzer.jitty.rest;

import javax.ws.rs.core.Response;

/**
 * Created by J. Melzer on 10.07.2016.
 */
public abstract class ControllerUtil {
    public static Response buildErrorResponse(String errorMessage) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessage(errorMessage)).build();
    }
}
