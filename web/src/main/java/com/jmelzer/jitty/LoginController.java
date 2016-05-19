package com.jmelzer.jitty;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by J. Melzer on 19.05.2016.
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
public class LoginController {

//    @GET
//    @Path("/resource")
//    public Map<String, Object> home() {
//        Map<String, Object> model = new HashMap<String, Object>();
//        model.put("id", UUID.randomUUID().toString());
//        model.put("content", "Hello World");
//        return model;
//    }
//    @POST
//    @Path("/user")
//    public Principal user(Principal user) {
//        return user;
//    }
}
