package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.config.SecurityUtil;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.service.PlayerService;
import com.jmelzer.jitty.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for Player
 */
@Component
@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerController {

    final static Logger LOG = LoggerFactory.getLogger(PlayerController.class);

    @Inject
    PlayerService service;
    @Inject
    TournamentService tournamentService;

    @Inject
    SecurityUtil securityUtil;

    @GET
    public List<TournamentPlayerDTO> getList() {
        LOG.info("query all TournamentPlayer ");
        return service.findAll();
    }

    @Path("{id}")
    @GET
    public TournamentPlayerDTO getById(@PathParam(value = "id") String id) {
        return service.findOne(Long.valueOf(id));

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public TournamentPlayer saveOrCreate(TournamentPlayer tournament) {
        return service.save(tournament);
    }

    @GET
    @Path("/possible-tournaments-classes")
    public List<TournamentClassDTO> possibleTournaments(@QueryParam("id") String playerId) {
        LOG.info("possible tournaments for player with id {}", playerId);
        TournamentPlayerDTO player = service.findOne(Long.valueOf(playerId));
        List<TournamentClassDTO> allList = tournamentService.getAllClasses(player, securityUtil.getActualUsername());
        List<TournamentClassDTO> retList = new ArrayList<>();
        for (TournamentClassDTO classDTO : allList) {
            if (!player.getClasses().contains(classDTO)) {
                retList.add(classDTO);
            }
        }
        return retList;
    }
}
