package com.jmelzer.jitty.rest;

import org.glassfish.jersey.server.monitoring.MonitoringStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for admin
 */
@Component
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdminController {

    final static Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @Inject
    Provider<MonitoringStatistics> statistics;

    @GET
    @Path("/stats")
    public String getStats() throws InterruptedException {
        final MonitoringStatistics monitoringStatistics = statistics.get();
        return monitoringStatistics.getRequestStatistics().toString();

    }

}
