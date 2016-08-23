package com.jmelzer.jitty.config;

import com.jmelzer.jitty.logging.MyApplicationEventListener;
import com.jmelzer.jitty.rest.*;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
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
                register(MyApplicationEventListener.class).
                register(TournamentClassController.class).
                register(PlayerController.class).
                register(ClubController.class).
                register(AssociationController.class).
                register(TournamentDirectorController.class).
                register(LiveViewController.class).
                register(DrawController.class).
                register(AdminController.class).
                register(TournamentController.class);
//        property(ServerProperties.TRACING, "ALL");
        property(ServerProperties.MONITORING_STATISTICS_ENABLED , true);
        property(CommonProperties.OUTBOUND_CONTENT_LENGTH_BUFFER, 10000);
    }

}