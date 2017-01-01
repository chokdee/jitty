package com.jmelzer.jitty.rest;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by J. Melzer on 26.12.2016.
 */
@Component
public class ErrorController {

    @GET
    @Path("/error1")
    public Response error() throws URISyntaxException {
        return Response.temporaryRedirect(new URI("/#/login")).build();
    }
}
