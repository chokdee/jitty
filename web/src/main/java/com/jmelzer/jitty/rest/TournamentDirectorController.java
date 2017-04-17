/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.config.SecurityUtil;
import com.jmelzer.jitty.exceptions.IntegrityViolation;
import com.jmelzer.jitty.model.dto.TableDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import com.jmelzer.jitty.service.QueueManager;
import com.jmelzer.jitty.service.TableManager;
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
    @Inject
    TableManager tableManager;
    @Inject
    QueueManager queueManager;

    @Path("/possible-games")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentSingleGameDTO> possibleGames() {
        return queueManager.listQueue(securityUtil.getActualTournamentId());
    }

    @Path("/running-games")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentSingleGameDTO> runningGames() {
        return queueManager.getBusyGames(securityUtil.getActualTournamentId());
    }

    @Path("/finished-games")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentSingleGameDTO> finishedGames() {
        return service.getFinishedGames(securityUtil.getActualTournamentId());
    }

    @Path("/start-game")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startGame(@QueryParam(value = "id") String id) {
        try {
            TournamentSingleGameDTO dto = service.startGame(Long.valueOf(id));
            return Response.ok(dto).build();
        } catch (IntegrityViolation integrityViolation) {
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
    }

    @Path("/start-possible-games")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startPossibleGames() {
        try {
            service.startPossibleGames(securityUtil.getActualTournamentId());
        } catch (IntegrityViolation integrityViolation) {
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
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
    public long saveResult(TournamentSingleGameDTO dto) {
        return service.saveAndFinishGame(dto);
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

    @Path("/tables")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TableDTO> tables() {
        return tableManager.getAllTables();
    }

    @Path("/save-table-count")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveTableCount(int tablecount) {
        try {
            service.saveTableCount(securityUtil.getActualUsername(), tablecount);
        } catch (IntegrityViolation integrityViolation) {
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
        return Response.ok().build();
    }
}
