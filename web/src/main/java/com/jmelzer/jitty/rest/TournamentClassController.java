package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.config.SecurityUtil;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.exceptions.IntegrityViolation;
import com.jmelzer.jitty.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
    @Inject
    SecurityUtil securityUtil;

    @Path("/not-running")
    @GET
    public List<TournamentClassDTO> getNotRunning() {
        return service.getAllClassesWithStatus(securityUtil.getActualUsername());
    }

    @Path("{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public TournamentClassDTO tournamentClass(@PathParam(value = "id") String id) {
        TournamentClassDTO clz = service.findOneClass(Long.valueOf(id));
        LOG.debug("found clz {}", clz);
        return clz;

    }

    @Path("{tid}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Long saveOrCreate(@PathParam(value = "tid") String tid,
                             TournamentClassDTO tournamentClass) {
        TournamentClass clz = service.addTC(Long.valueOf(tid), tournamentClass);
        return clz.getId();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(TournamentClassDTO tournamentClass) {
        service.updateClass(tournamentClass);
        return Response.ok().build();
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam(value = "id") String id) {
        LOG.info("delete class {}", id);
        try {
            service.deleteClass(Long.valueOf(id));
        } catch (EmptyResultDataAccessException e) {
            LOG.warn("tc with id {} not found ", id);
            return null;
        } catch (NumberFormatException e) {
            LOG.warn("id is not a number ", id);
            return null;
        } catch (IntegrityViolation integrityViolation) {
            LOG.warn("not possible to delete the class {} error message is '{}'" , id, integrityViolation.getMessage());
            return ControllerUtil.buildErrorResponse(integrityViolation.getMessage());
        }
        return Response.ok().build();

    }
}
