package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.dao.ClubRepository;
import com.jmelzer.jitty.model.Club;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for Player
 */
@Component
@Path("/clubs")
@Produces(MediaType.APPLICATION_JSON)
public class ClubController {

    final static Logger LOG = LoggerFactory.getLogger(ClubController.class);

    @Inject
    ClubRepository repository;

    @GET
    public List<Club> getList() {
        LOG.info("query all Clubs");
        return repository.findAll();
    }

}
