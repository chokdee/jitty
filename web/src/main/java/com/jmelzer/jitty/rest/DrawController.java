/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.exceptions.IntegrityViolation;
import com.jmelzer.jitty.model.PhaseCombination;
import com.jmelzer.jitty.model.TournamentSystemType;
import com.jmelzer.jitty.model.dto.*;
import com.jmelzer.jitty.service.DrawGroupManager;
import com.jmelzer.jitty.service.DrawKoFieldManager;
import com.jmelzer.jitty.service.SwissSystemManager;
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
    SwissSystemManager swissSystemManager;

    @Inject
    TournamentService tournamentService;

    @Path("/player-for-class")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentPlayerDTO> getPlayerforClass(@QueryParam(value = "cid") String id) {
        List<TournamentPlayerDTO> list = tournamentService.getPlayerforClass(Long.valueOf(id));
        LOG.debug("found player {}", list.size());
        return list;

    }

    @Path("/possible-player-for-groups")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentPlayerDTO> getPossiblePlayerForGroups(@QueryParam(value = "cid") String id) {
        return drawGroupManager.getPossiblePlayerForGroups(Long.valueOf(id));

    }

    @Path("/possible-player-swiss-system")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentPlayerDTO> getPossiblePlayerForSwissSystem(@QueryParam(value = "cid") String id) {
        return swissSystemManager.getRanking(Long.valueOf(id));
    }

    @Path("/calc-swiss-draw")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public SwissDraw calcSwissDraw(@QueryParam(value = "cid") String id, @QueryParam(value = "round") String sRound) {
        Long cid = Long.valueOf(id);
        return swissSystemManager.calcDraw(cid, Integer.valueOf(sRound));
    }

    @Path("/get-swiss-draw")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public SwissDraw getSwissDraw(@QueryParam(value = "cid") String id, @QueryParam(value = "round") String sRound) {
        Long cid = Long.valueOf(id);
        return swissSystemManager.getDraw(cid, Integer.valueOf(sRound));
    }


    @Path("/start-swiss-class")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startSwissRound(@QueryParam(value = "cid") String id) {
        try {
            swissSystemManager.startClass(Long.valueOf(id));
        } catch (IntegrityViolation integrityViolation) {
            LOG.warn("not possible to start the class {}, error message is '{}' ", id, integrityViolation.getMessage());
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
        return Response.ok().build();
    }


    @Path("/create-next-swiss-round-if-necessary")
    @GET
    public Response createtNextSwissRoundIfNecessary(@QueryParam(value = "cid") String id, @QueryParam(value = "round") String sRound) {
        try {
            return Response.ok(swissSystemManager.createtNextSwissRoundIfNecessary(Long.valueOf(id), Integer.valueOf(sRound))).build();
        } catch (IntegrityViolation integrityViolation) {
            LOG.warn("not possible to create next round {} for class {} error message is '{}'", sRound, id, integrityViolation.getMessage());
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
    }

    @Path("/start-swiss-round")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startSwissRound(
            @QueryParam(value = "cid") String id,
            @QueryParam(value = "round") String sRound) {
        try {
            swissSystemManager.start(Long.valueOf(id), Integer.valueOf(sRound));
        } catch (IntegrityViolation integrityViolation) {
            LOG.warn("not possible to start the round {} for class {} error message is '{}'", sRound, id, integrityViolation.getMessage());
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
        return Response.ok().build();
    }

    @Path("/save-swiss-round")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSwissRound(
            @QueryParam(value = "cid") String id,
            @QueryParam(value = "round") String sRound,
            List<TournamentSingleGameDTO> games) {
        try {
            swissSystemManager.save(Long.valueOf(id), Integer.valueOf(sRound), games);
        } catch (IntegrityViolation integrityViolation) {
            LOG.warn("not possible to start the round {} for class {} error message is '{}'", sRound, id, integrityViolation.getMessage());
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
        return Response.ok().build();
    }

    @Path("/swiss-round")
    @GET
    public int round(@QueryParam(value = "cid") String id) {
        return swissSystemManager.getRoundNr(Long.valueOf(id));
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
    public Response start(@QueryParam(value = "cid") String sid) {
        Long id = Long.valueOf(sid);
        TournamentClassDTO tc = tournamentService.findOneClass(id);
        TournamentSystemType stype = TournamentSystemType.enumOf(tc.getSystemType());
        try {
            assert stype != null;

            switch (stype) {
                case GK:
                    drawGroupManager.startClass(id);
                    break;
                case AC:
                    swissSystemManager.startClass(id);
                    break;
                default:
                    throw new IntegrityViolation("unkown type " + stype);

            }
        } catch (IntegrityViolation integrityViolation) {
            LOG.warn("not possible to start the class {} error message is '{}'", id, integrityViolation.getMessage());
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
        return Response.ok().build();
    }

    @Path("/select-phase-combination")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response selectPhaseCombination(@QueryParam(value = "cid") String id, @QueryParam(value = "type") String type) {
        tournamentService.selectPhaseCombination(Long.valueOf(id), PhaseCombination.enumOf(Integer.valueOf(type)));
        return Response.ok().build();
    }

    @Path("/actual-phase")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public PhaseDTO actualPhase(@QueryParam(value = "cid") String id) {
        return tournamentService.actualPhase(Long.valueOf(id));
    }

    @Path("/calc-optimal-group-size")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public GroupPhaseDTO calcOptimalGroupSize(GroupPhaseDTO dto) {
        return drawGroupManager.calcOptimalGroupSize(dto);
    }

    @Path("/automatic-draw")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public GroupPhaseDTO automaticDraw(@QueryParam(value = "cid") String id, GroupPhaseDTO dto) {
        return drawGroupManager.automaticDraw(Long.valueOf(id), dto);

    }

    @Path("/swiss/calc-ranking-first-round")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public List<TournamentPlayerDTO> calcRankingFirstRound(List<TournamentPlayerDTO> players) {
        return swissSystemManager.calcRankingFirstRound(players);

    }

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public TournamentClassDTO save(TournamentClassDTO dto) {
        return tournamentService.updateClass(dto);
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

    @Path("/get-ko")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public KOFieldDTO getKOForClz(@QueryParam(value = "cid") String id) {
        return tournamentService.getKOForClz(Long.valueOf(id));
    }
}
