package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.User;
import com.jmelzer.jitty.model.dto.TournamentDTO;
import com.jmelzer.jitty.service.TournamentService;
import com.jmelzer.jitty.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for Tournament
 */
@Component
@Path("/tournaments")
@Produces(MediaType.APPLICATION_JSON)
public class TournamentController {

    final static Logger LOG = LoggerFactory.getLogger(TournamentController.class);

    @Inject
    UserRepository userRepository;

    @Inject
    TournamentService service;

    @Inject
    UserService userService;

    @GET
    public List<TournamentDTO> getList() {
        LOG.info("query all Tournament ");
        return service.findAll();
    }

    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TournamentDTO tournament(@PathParam(value = "id") String id) {
        return service.findOne(Long.valueOf(id));

    }

    @Path("actual/{id}")
    @GET
    public Response select(@PathParam(value = "id") String id) {
        //todo get the right one from spring security
        User user = userRepository.findOne(1L);
        userService.selectTournamentForUser(user, id);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveOrCreate(TournamentDTO tournament) {
        if (tournament.getId() == null) {
            service.create(tournament);
        } else {
            service.update(tournament);
        }
        return Response.ok().build();
    }


}
