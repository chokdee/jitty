package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by J. Melzer on 26.07.2016.
 */
@Component
@Path("/draw")
@Produces(MediaType.APPLICATION_JSON)
public class DrawController {

    final static Logger LOG = LoggerFactory.getLogger(DrawController.class);

    @Inject
    TournamentService service;

    @Path("/player-for-class")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentPlayerDTO> getPlayerforClass(@QueryParam(value = "cid") String id) {
        List<TournamentPlayerDTO>  list = service.getPlayerforClass(Long.valueOf(id));
        LOG.debug("found player {}", list.size());
        return list;

    }
}
