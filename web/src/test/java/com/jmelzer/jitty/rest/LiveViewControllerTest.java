/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.dto.GroupResultDTO;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;

import static com.jmelzer.jitty.rest.TestUtil.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 26.07.2016.
 * Test draw controller
 */
@DirtiesContext
public class LiveViewControllerTest extends SecureResourceTest {
    @Ignore("must create tournament before")
    @Test
    public void getStartedClasses() throws Exception {
        try {
            doLogin();

            ResponseEntity<TournamentClassDTO[]> entity = http(HttpMethod.GET, "api/lifeview/started-classes",
                    createHttpEntity(null, loginHeaders), TournamentClassDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().length, is(greaterThan(0)));
        } catch (HttpClientErrorException e) {
            fail(e.getResponseBodyAsString());
        }
    }

    @Test
    @Ignore("this mus create own testdata")
    public void getGroups() throws Exception {
        try {
            doLogin();

            ResponseEntity<GroupResultDTO[]> entity = http(HttpMethod.GET, "api/lifeview/groups?cid=2",
                    createHttpEntity(null, loginHeaders), GroupResultDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().length, is(4));
        } catch (HttpClientErrorException e) {
            fail(e.getResponseBodyAsString());
        }
    }
}