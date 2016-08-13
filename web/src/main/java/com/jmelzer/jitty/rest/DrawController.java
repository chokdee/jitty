package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.TournamentSingleGame;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
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

    @Path("/start")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response start(@QueryParam(value = "cid") String id) {
        service.startClass(Long.valueOf(id));
        return Response.ok().build();
    }


    @Path("/calc-optimal-group-size")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public TournamentClassDTO calcOptimalGroupSize(TournamentClassDTO dto) {
        dto = service.calcOptimalGroupSize(dto);
        return dto;
    }

    @Path("/automatic-draw")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public TournamentClassDTO automaticDraw(TournamentClassDTO dto) {
        dto = service.automaticDraw(dto);
        return dto;
    }

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(TournamentClassDTO dto) {
        service.updateClass(dto);
        dto = service.findOneClass(dto.getId());
        return Response.ok().build();
    }

    @Path("/dummy-player")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response dummyPlayer(@QueryParam(value = "cid") String id) {
        service.createDummyPlayer(Long.valueOf(id));
        return Response.ok().build();
    }

}
