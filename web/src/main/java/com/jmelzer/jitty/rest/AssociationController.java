package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.dao.AssociationRepository;
import com.jmelzer.jitty.model.Association;
import com.jmelzer.jitty.model.Club;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by J. Melzer on 16.03.2016.
 * REST API for Association
 */
@Component
@Path("/associations")
@Produces(MediaType.APPLICATION_JSON)
public class AssociationController {

    final static Logger LOG = LoggerFactory.getLogger(AssociationController.class);

    @Inject
    AssociationRepository repository;

    @GET
    public List<Association> getList() {
        LOG.info("query all Associations");
        return repository.findAll();
    }

}
