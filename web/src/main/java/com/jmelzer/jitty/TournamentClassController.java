package com.jmelzer.jitty;

import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.service.IntegrationViolation;
import com.jmelzer.jitty.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for User
 */
@Component
@Path("/tournament-classes")
@Produces(MediaType.APPLICATION_JSON)
public class TournamentClassController {

    final static Logger LOG = LoggerFactory.getLogger(TournamentClassController.class);

    @Inject
    TournamentService service;


    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TournamentClass tournamentClass(@PathParam(value = "id") String id) {
        TournamentClass clz = service.findOneClass(Long.valueOf(id));
        LOG.debug("found clz {}", clz);
        return clz;

    }
    @Path("{tid}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public TournamentClass saveOrCreate(@PathParam(value = "tid") String tid,
                                        TournamentClass tournamentClass) {
        Tournament t = service.findOne(Long.valueOf(tid));
        t.addClass(tournamentClass);
        service.update(t);
        return tournamentClass;
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam(value = "id") String id) {
        LOG.info("delete class {}", id);
//        if (true)
//            return ControllerUtil.buildErrorResponse("Nicht möglich zu lsöchen");
        try {
            service.deleteClass(Long.valueOf(id));
        } catch (EmptyResultDataAccessException e) {
            LOG.warn("tc with id {} not found ", id);
            return null;
        } catch (NumberFormatException e) {
            LOG.warn("id not a number ", id);
            return null;
        } catch (IntegrationViolation integrationViolation) {
            LOG.warn("", integrationViolation);
            return ControllerUtil.buildErrorResponse(integrationViolation.getMessage());
        }
        return Response.ok().build();

    }
}
