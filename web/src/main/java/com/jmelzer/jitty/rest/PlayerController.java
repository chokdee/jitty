/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.config.SecurityUtil;
import com.jmelzer.jitty.exceptions.IntegrityViolation;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.service.PlayerService;
import com.jmelzer.jitty.service.TournamentService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for Player
 */
@Component
@Path("/players")

public class PlayerController {

    final static Logger LOG = LoggerFactory.getLogger(PlayerController.class);

    @Inject
    PlayerService service;

    @Inject
    SecurityUtil securityUtil;

    @Inject
    private TournamentService tournamentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TournamentPlayerDTO> getList() {
        LOG.info("query all TournamentPlayer ");
        return service.findAll(securityUtil.getActualTournamentId());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TournamentPlayerDTO getById(@PathParam(value = "id") String id) {
        return service.findOne(Long.valueOf(id));

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveOrCreate(TournamentPlayerDTO player) {
        service.save(player, securityUtil.getActualTournamentId());
        return Response.ok().build();
    }

    //todo chanmedige to qttr value
    @POST
    @Path("/possible-tournaments-classes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TournamentClassDTO> possibleTournaments(TournamentPlayerDTO playerDTO) {

        LOG.info("possible tournaments for player {}", playerDTO);

        if (checkNull(playerDTO)) {
            return new ArrayList<>();
        }
//        TournamentPlayerDTO player = service.findOne(Long.valueOf(playerId));
        return tournamentService.getAllClasses(playerDTO, securityUtil.getActualUsername());
    }

    private boolean checkNull(TournamentPlayerDTO playerDTO) {
        return playerDTO == null;
    }

    @POST
    @Path("/import-from-click-tt")  //Your Path or URL to call this service
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(
            @QueryParam(value = "assignWhileImport") String assignWhileImportS,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
        Boolean assignWhileImport = Boolean.valueOf(assignWhileImportS);
        int count = service.importPlayerFromClickTT(fileInputStream, securityUtil.getActualTournamentId(), assignWhileImport);
        return Response.status(Response.Status.OK).entity("Es wurden " + count + " Spieler importiert").build();
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam(value = "id") String id) {
        LOG.info("delete class {}", id);
        try {
            service.delete(Long.valueOf(id));
        } catch (EmptyResultDataAccessException e) {
            LOG.warn("player with id {} not found ", id);
            return null;
        } catch (NumberFormatException e) {
            LOG.warn("id is not a number ", id);
            return null;
        } catch (IntegrityViolation integrityViolation) {
            LOG.warn("not possible to delete the class {} error message is '{}'", id, integrityViolation.getMessage());
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
        return Response.ok().build();

    }
}
