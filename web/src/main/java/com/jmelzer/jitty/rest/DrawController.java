package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.dto.KOFieldDTO;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.service.DrawGroupManager;
import com.jmelzer.jitty.service.DrawKoFieldManager;
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
    DrawKoFieldManager drawKoFieldManager;
    @Inject
    DrawGroupManager drawGroupManager;

    @Inject
    TournamentService tournamentService;

    @Path("/player-for-class")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentPlayerDTO> getPlayerforClass(@QueryParam(value = "cid") String id) {
        List<TournamentPlayerDTO>  list = tournamentService.getPlayerforClass(Long.valueOf(id));
        LOG.debug("found player {}", list.size());
        return list;

    }
    @Path("/possible-player-for-kofield")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentPlayerDTO> getPossiblePlayerForKOField(@QueryParam(value = "cid") String id) {
        return drawKoFieldManager.getPossiblePlayerForKOField(Long.valueOf(id));

    }

    @Path("/calc-ko-size")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public int calcKOSizeInInt(@QueryParam(value = "cid") String id) {
        return drawKoFieldManager.calcKOSizeInInt(Long.valueOf(id));

    }

    @Path("/start")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response start(@QueryParam(value = "cid") String id) {
        drawGroupManager.startClass(Long.valueOf(id));
        return Response.ok().build();
    }


    @Path("/calc-optimal-group-size")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public TournamentClassDTO calcOptimalGroupSize(TournamentClassDTO dto) {
        return drawGroupManager.calcOptimalGroupSize(dto);
    }

    @Path("/automatic-draw")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public TournamentClassDTO automaticDraw(TournamentClassDTO dto) {
        return drawGroupManager.automaticDraw(dto);
    }

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(TournamentClassDTO dto) {
        tournamentService.updateClass(dto);
        return Response.ok().build();
    }

    @Path("/dummy-player")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response dummyPlayer(@QueryParam(value = "cid") String id) {
        tournamentService.createDummyPlayer(Long.valueOf(id));
        return Response.ok().build();
    }

    @Path("/draw-ko")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public KOFieldDTO startKO(@QueryParam(value = "id") String id, @QueryParam(value = "assignPlayer") Boolean assignPlayer) {
        return drawKoFieldManager.drawKO(Long.valueOf(id), assignPlayer);
    }

    @Path("/start-ko")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startKO(@QueryParam(value = "cid") String id) {
        drawKoFieldManager.startKOField(Long.valueOf(id));
        return Response.ok().build();
    }
}
