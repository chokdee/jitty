/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.config.SecurityUtil;
import com.jmelzer.jitty.model.dto.TableSettingsDTO;
import com.jmelzer.jitty.service.SettingsManager;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by J. Melzer on 31.10.2017.
 */
@Component
@Path("/settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsController {
    @Inject
    SettingsManager settingsManager;

    @Inject
    SecurityUtil securityUtil;

    @Path("/table-settings")
    @GET
    public TableSettingsDTO getTableSettings() {
        return settingsManager.getTableSettings(securityUtil.getActualTournament());
    }

}
