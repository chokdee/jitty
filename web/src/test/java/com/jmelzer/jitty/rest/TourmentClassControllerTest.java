/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.AgeGroup;
import com.jmelzer.jitty.model.ErrorMessage;
import com.jmelzer.jitty.model.TournamentSystemType;
import com.jmelzer.jitty.model.dto.GroupPhaseDTO;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import org.junit.Ignore;
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
    @Ignore("wrong test. mustcreate a tournament class first")
    public void testDeleteWithErrors() throws Exception {

        thrown.expect(HttpClientErrorException.class);
        thrown.expectMessage("400");

        http(HttpMethod.DELETE, "api/tournament-classes/2",
                createHttpEntity(null, loginHeaders), ErrorMessage.class);

    }

    @Test
    public void testDelete() throws Exception {

        Long tid = createTournament("delete", 8, 2);
        TournamentClassDTO classDTO = createClz(tid);
        http(HttpMethod.DELETE, "api/tournament-classes/" + classDTO.getId(),
                createHttpEntity(null, loginHeaders), ErrorMessage.class);

        thrown.expect(HttpClientErrorException.class);
        thrown.expectMessage("404");
        ResponseEntity<ErrorMessage> errorMessage = http(HttpMethod.GET, "api/tournament-classes/" + classDTO.getId(),
                createHttpEntity(null, loginHeaders), ErrorMessage.class);
        assertEquals(404, errorMessage.getStatusCode().value());
    }

    private TournamentClassDTO createClz(Long tId) {
        TournamentClassDTO tournamentClass = new TournamentClassDTO();
        tournamentClass.setName("test");
        tournamentClass.setStartTTR(0);
        tournamentClass.setEndTTR(3000);
        tournamentClass.setAgeGroup(AgeGroup.DH.getValue());
        tournamentClass.setSystemType(TournamentSystemType.AC.getValue());

        ResponseEntity<Long> longEntitiy = http(HttpMethod.POST, "api/tournament-classes/" + tId,
                createHttpEntity(tournamentClass, loginHeaders), Long.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
        Long tClassId = longEntitiy.getBody();
        assertNotNull(tClassId);

        ResponseEntity<GroupPhaseDTO> phaseDTO = http(HttpMethod.GET, "api/draw/actual-phase?cid=" + tClassId,
                createHttpEntity(null, loginHeaders), GroupPhaseDTO.class);

        assertNull("still not drawed", phaseDTO.getBody());
        //return fresh copy
        return http(HttpMethod.GET, "api/tournament-classes/" + tClassId,
                createHttpEntity(null, loginHeaders), TournamentClassDTO.class).getBody();
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
            tournamentClass.setAgeGroup(AgeGroup.DH.getValue());
            tournamentClass.setSystemType(TournamentSystemType.GK.getValue());

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