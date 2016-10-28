package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.config.SecurityUtil;
import com.jmelzer.jitty.model.dto.KOFieldDTO;
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
    @Inject
    SecurityUtil securityUtil;


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

    @Path("/move-game-back-to-possiblegames")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response moveGameBackToPossiblegames(@QueryParam(value = "id") String id) {
        service.moveGameBackToPossiblegames(Long.valueOf(id));
        return Response.ok().build();
    }

    @Path("/get-game-for-printing")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public TournamentSingleGameDTO getGame(@QueryParam(value = "id") String id) {
        return service.getGame(Long.valueOf(id));
    }

    @Path("/save-result")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveResult(TournamentSingleGameDTO dto) {
        service.saveAndFinishGame(dto);
        return Response.ok().build();
    }

    @Path("/groups-finished")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean areAllGroupsFinished(@QueryParam(value = "id") String id) {
        return service.areAllGroupsFinished(Long.valueOf(id));
    }

    @Path("/any-phase-finished")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Long[] anyPhaseFinished() {
        return service.anyPhaseFinished(securityUtil.getActualUsername());
    }

    @Path("/start-ko")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public KOFieldDTO startKO(@QueryParam(value = "id") String id, @QueryParam(value = "assignPlayer") Boolean assignPlayer) {
        return service.startKO(Long.valueOf(id), assignPlayer);
    }
}
