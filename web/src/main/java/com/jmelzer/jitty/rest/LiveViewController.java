/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.config.SecurityUtil;
import com.jmelzer.jitty.model.dto.GroupResultDTO;
import com.jmelzer.jitty.model.dto.SwissResultsDTO;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.service.SwissSystemManager;
import com.jmelzer.jitty.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for Liveview
 */
@Component
@Path("/lifeview")
@Produces(MediaType.APPLICATION_JSON)
public class LiveViewController {

    final static Logger LOG = LoggerFactory.getLogger(LiveViewController.class);

    @Inject
    SwissSystemManager swissSystemManager;

    @Inject
    TournamentService service;
    @Inject
    SecurityUtil securityUtil;

    @Path("/started-classes")
    @GET
    public List<TournamentClassDTO> getNotRunning() {
        return service.getStartedClasses(securityUtil.getActualUsername());
    }

    @Path("/groups")
    @GET
    public List<GroupResultDTO> getGroups(@QueryParam(value = "cid") String id) {
        return service.getGroupResults(Long.valueOf(id));
    }

    @Path("/swiss-results")
    @GET
    public SwissResultsDTO getSwissResults(@QueryParam(value = "cid") String id) {
        Long cid = Long.valueOf(id);
        return swissSystemManager.getResults(cid);
    }
}
