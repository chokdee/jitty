package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.config.SecurityUtil;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.service.PlayerService;
import com.jmelzer.jitty.service.TournamentService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
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
        return service.findAll( securityUtil.getActualTournament());
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
        service.save(player, securityUtil.getActualTournament());
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
        if (playerDTO == null) {
            return true;
        }
        return false;
    }

    @POST
    @Path("/import-from-click-tt")  //Your Path or URL to call this service
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadFile(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
        int count = service.importPlayerFromClickTT(fileInputStream, securityUtil.getActualTournament());
        return Response.status(Response.Status.OK).entity("Es wurden " + count + " Spieler importiert").build();
    }
}
