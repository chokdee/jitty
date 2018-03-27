/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.Association;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static com.jmelzer.jitty.rest.TestUtil.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
/**
 * Created by J. Melzer on 19.07.2016.
 * Test club controller
 */

public class AssociationControllerTest extends SecureResourceTest {

    @Test
    public void testGetList() throws Exception {
        try {
            doLogin();

            ResponseEntity<Association[]> entity = http(HttpMethod.GET, "api/associations",
                    createHttpEntity(null, loginHeaders), Association[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody()[0].getId(), is(1L));

        } catch (HttpClientErrorException e) {
            fail(e.getResponseBodyAsString());
        }
    }

}