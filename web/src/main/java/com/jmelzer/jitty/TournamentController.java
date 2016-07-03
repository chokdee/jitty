package com.jmelzer.jitty;

import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.User;
import com.jmelzer.jitty.service.TournamentService;
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
 * REST API for User
 */
@Component
@Path("/tournaments")
@Produces(MediaType.APPLICATION_JSON)
public class TournamentController {

    final static Logger LOG = LoggerFactory.getLogger(TournamentController.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Inject
    TournamentRepository repository;

    @Inject
    UserRepository userRepository;

    @Inject
    TournamentService service;

    @GET
    public List<Tournament> getList() {
        LOG.info("query all Tournament ");
        return service.findAll();
    }

    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Tournament tournament(@PathParam(value = "id") String id) {
        return service.findOne(Long.valueOf(id));

    }

    @Path("actual/{id}")
    @GET
    public Tournament select(@PathParam(value = "id") String id) {
        User user = userRepository.findOne(1L);
        Tournament t = repository.findOne(Long.valueOf(id));
        user.setLastUsedTournament(t);
        userRepository.saveAndFlush(user);
        return t;
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Tournament create(Tournament tournament) {
        return repository.save(tournament);
    }


}
