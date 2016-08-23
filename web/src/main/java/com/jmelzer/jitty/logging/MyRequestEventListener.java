package com.jmelzer.jitty.logging;

import org.glassfish.jersey.server.internal.routing.UriRoutingContext;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyRequestEventListener implements RequestEventListener {
    final static Logger LOG = LoggerFactory.getLogger(MyRequestEventListener.class);

    private final int requestNumber;
    private final long startTime;

    public MyRequestEventListener(int requestNumber) {
        this.requestNumber = requestNumber;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onEvent(RequestEvent event) {
        switch (event.getType()) {
            case RESOURCE_METHOD_START:
                LOG.debug("Resource method "
                        + event.getUriInfo().getRequestUri() + " - "
                        + ((UriRoutingContext)event.getUriInfo()).getEndpoint()
                        + " started for request " + requestNumber);
                break;
            case FINISHED:
                LOG.debug("Request " + requestNumber
                        + " finished. Processing time "
                        + (System.currentTimeMillis() - startTime) + " ms.");
                break;
        }
    }
}