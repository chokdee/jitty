package com.jmelzer.jitty;

import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.model.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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

    @GET
    public List<Tournament> getUserList() {
        LOG.info("query all Tournament ");
        return repository.findAll();
    }

    @Path("{id}")
    @GET
    public Tournament tournament(@PathParam(value = "id") String id) {
        return repository.findOne(Long.valueOf(id));

    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Tournament create(Tournament tournament) {
        return repository.save(tournament);
    }


}
