package com.jmelzer.jitty;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
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
        register(UserController.class).register(TournamentController.class);
    }
}