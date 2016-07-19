package com.jmelzer.jitty.config;

import com.jmelzer.jitty.rest.*;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

/**
 * Created by J. Melzer on 19.05.2016.
 * Jersey Spring init
 */
@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(UserController.class).
                register(TournamentClassController.class).
                register(PlayerController.class).
                register(ClubController.class).
                register(TournamentController.class);
    }
}