package com.jmelzer.jitty;

import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for User
 */
@Component
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    final static Logger LOG = LoggerFactory.getLogger(UserController.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    UserRepository repository;

    @GET
    public List<User> getUserList() {
        LOG.info("query all user ");
        return repository.findAll();
    }

    @Path("{id}")
    @GET
    public User users(@PathParam(value = "id") String id) {
        return repository.findOne(Long.valueOf(id));

    }

    @Path("{id}")
    @DELETE
    public String delete(@PathParam(value = "id") String id) {
        LOG.info("delete user {}", id);
        try {
            repository.delete(Long.valueOf(id));
        } catch (EmptyResultDataAccessException e) {
            LOG.warn("user with id {} not found ", id);
            return null;
        } catch (NumberFormatException e) {
            LOG.warn("id not a number ", id);
            return null;
        }
        return id;

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public User create(User user) {
        return repository.save(user);
    }

    @POST
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(User user) {
        LOG.info("pw change for user {}", user.getId());
        User userDB = repository.findOne(user.getId());
        userDB.setPassword(user.getPassword());
        repository.save(userDB);
        return Response.ok().build();
    }

}
