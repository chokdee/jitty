package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import com.jmelzer.jitty.service.TournamentService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by J. Melzer on 13.08.2016.
 * Manage the tournament
 */
@Component
@Path("/tournamentdirector")
@Produces(MediaType.APPLICATION_JSON)
public class TournamentDirectorController {

    @Inject
    TournamentService service;

    @Path("/possible-games")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentSingleGameDTO> possibleGames() {
        return service.listQueue();
    }

    @Path("/running-games")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentSingleGameDTO> runningGames() {
        return service.getBusyGames();
    }

    @Path("/finished-games")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentSingleGameDTO> finishedGames() {
        return service.getFinishedGames();
    }

    @Path("/start-game")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startGame(@QueryParam(value = "id") String id) {
        service.startGame(Long.valueOf(id));
        return Response.ok().build();
    }

    @Path("/save-result")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveResult(TournamentSingleGameDTO dto) {
        service.saveGame(dto);
        service.finishGame(dto);
        return Response.ok().build();
    }
}
