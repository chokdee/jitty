package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.Tournament;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
public class TourmentControllerTest extends SecureResourceTest {

    @Test
    public void testGetTournament() throws Exception {
        HttpHeaders loginHeaders = doLogin();

        ResponseEntity<Tournament> entity = http(HttpMethod.GET, "api/tournaments/2",
                createHttpEntity(null, loginHeaders), Tournament.class);

        System.out.println();
        System.out.println("-----------------");
        System.out.println("entity.getBody() = " + entity.getBody());
        System.out.println("-----------------");
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        assertEquals((Long) 2L, entity.getBody().getId());
    }


    @Test
    public void testsave() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();
            ResponseEntity<String> okResponse = restTemplate.exchange(
                    "http://localhost:9999/resource",
                    HttpMethod.GET,
                    createHttpEntity(null, loginHeaders),
                    String.class);

            assertThat(okResponse.getStatusCode(), is(HttpStatus.OK));

            Tournament tournament = new Tournament();
            tournament.setName("dummy");
            tournament.setStartDate(new Date());
            tournament.setEndDate(new Date());
            String uri = "http://localhost:9999/api/tournaments";
            Tournament result = restTemplate.postForObject(uri,
                    createHttpEntity(tournament, okResponse.getHeaders()), Tournament.class);
            System.out.println();
            System.out.println("-----------------");
            System.out.println("result = " + result);
            System.out.println("-----------------");
            assertNotNull(result.getId());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }
}