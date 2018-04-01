/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */
package com.jmelzer.jitty.config;

import com.jmelzer.jitty.exceptions.NotFoundException;
import com.jmelzer.jitty.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 */
@Provider
public class DefaultExceptionHandler implements ExceptionMapper<Throwable> {

    static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public Response toResponse(Throwable e) {

        logger.error("", e);

        Response.ResponseBuilder builder = Response.serverError();
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        return builder.status(gettHttpStatus(e)).entity(errorMessage).build();

    }

    private int gettHttpStatus(Throwable ex) {
        if (ex instanceof NotFoundException) {
            return 404;
        } else {
            return 500;
        }
    }
}
