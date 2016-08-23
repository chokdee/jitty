package com.jmelzer.jitty.logging;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyApplicationEventListener
        implements ApplicationEventListener {
    private volatile int requestCnt = 0;

    final static Logger LOG = LoggerFactory.getLogger(MyApplicationEventListener.class);


    @Override
    public void onEvent(ApplicationEvent event) {
        switch (event.getType()) {
            case INITIALIZATION_FINISHED:
                LOG.debug("Application "
                        + event.getResourceConfig().getApplicationName()
                        + " was initialized.");
                break;
            case DESTROY_FINISHED:
                LOG.debug("Application "
                        + event.getResourceConfig().getApplicationName() + " destroyed.");
                break;
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        requestCnt++;
        System.out.println("Request " + requestCnt + " started.");
        // return the listener instance that will handle this request.
        return new MyRequestEventListener(requestCnt);
    }
}