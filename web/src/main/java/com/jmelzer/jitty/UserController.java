package com.jmelzer.jitty;

import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.User;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for User
 */
@Component
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    UserRepository repository;

    @GET
    public List<User> getUserList() {
        return repository.findAll();
    }

    @Path("{id}")
    @GET
    public User users(@PathParam(value = "id") String id) {
        return repository.findOne(Long.valueOf(id));

    }
}
