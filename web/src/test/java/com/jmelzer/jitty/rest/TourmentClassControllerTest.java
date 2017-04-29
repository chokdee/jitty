/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static com.jmelzer.jitty.rest.TestUtil.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
/**
 * Created by J. Melzer on 19.05.2016.
 * Test TourmentClassController
 */
public class TourmentClassControllerTest extends SecureResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testGetTournamentClass() throws Exception {
        try {

            ResponseEntity<TournamentClassDTO> entity = http(HttpMethod.GET, "api/tournament-classes/2",
                    createHttpEntity(null, loginHeaders), TournamentClassDTO.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().getId(), is(2L));
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    public void testDeleteWithErrors() throws Exception {

        thrown.expect(HttpClientErrorException.class);
        thrown.expectMessage("400");

        http(HttpMethod.DELETE, "api/tournament-classes/2",
                createHttpEntity(null, loginHeaders), ErrorMessage.class);

    }

    @Test
    public void getNotRunning() throws Exception {
        try {
            createTournament("not running", 4, 2);
            ResponseEntity<TournamentClassDTO[]> entity = http(HttpMethod.GET, "api/tournament-classes/classes-with-status",
                    createHttpEntity(null, loginHeaders), TournamentClassDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            //fixme sometime if other test were running before we got exception here
            assertThat(entity.getBody().length, is(1));
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    public void testSaveNewTC() throws Exception {
        try {
            long id = createTournament("testSaveNewTC", 4, 2);

            ResponseEntity<String> okResponse = restTemplate.exchange(
                    "http://localhost:9999/resource",
                    HttpMethod.GET,
                    createHttpEntity(null, loginHeaders),
                    String.class);

            assertThat(okResponse.getStatusCode(), is(HttpStatus.OK));


            TournamentClassDTO tournamentClass = new TournamentClassDTO();
            tournamentClass.setName("dummy");
            tournamentClass.setStartTTR(0);
            tournamentClass.setEndTTR(1000);

            ResponseEntity<Void> entity = http(HttpMethod.POST, "api/tournament-classes/" + id,
                    createHttpEntity(tournamentClass, okResponse.getHeaders()), Void.class);

            assertThat(entity.getStatusCode(), is(HttpStatus.OK));

            assertThat(jdbcTemplate.queryForObject("select count(*) from tournament_class where t_id = " + id, Integer.class), is(2));
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

}