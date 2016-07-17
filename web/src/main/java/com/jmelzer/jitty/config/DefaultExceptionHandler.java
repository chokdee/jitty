/*
 *  Project: TRIGGERDIALOG 2.0.0
 *  Copyright(c) 2011-2016 by Deutsche Post AG
 *  All rights reserved.
 */
package com.jmelzer.jitty.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 */
@Provider
@Component
public class DefaultExceptionHandler implements ExceptionMapper<Exception> {

    static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public Response toResponse(Exception e) {

        logger.error("", e);

        Response.ResponseBuilder builder = Response.serverError();
        return builder.build();

    }

}
