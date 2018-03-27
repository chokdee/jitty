/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.jmelzer.jitty.rest.TestUtil.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test player controller
 */
public class PlayerControllerTest extends SecureResourceTest {


    @Test
    public void testGetList() throws Exception {
        try {

            createTournament("playertest1", 4, 2);
            ResponseEntity<TournamentPlayerDTO[]> entity = http(HttpMethod.GET, "api/players",
                    createHttpEntity(null, loginHeaders), TournamentPlayerDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().length, is(0));

        } catch (HttpClientErrorException e) {
            fail(e.getResponseBodyAsString());
        }
    }

    @Test
    public void testGetOne() throws Exception {
        try {
            ResponseEntity<TournamentPlayerDTO> entity = http(HttpMethod.GET, "api/players/2",
                    createHttpEntity(null, loginHeaders), TournamentPlayerDTO.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().getId(), is(2L));

        } catch (HttpClientErrorException e) {
            fail(e.getResponseBodyAsString());
        }
    }

    @Test
    public void possibleTournaments() throws Exception {
        try {
            TournamentPlayerDTO playerDTO = new TournamentPlayerDTO();
            ResponseEntity<TournamentClassDTO[]> entity = http(HttpMethod.POST, "api/players/possible-tournaments-classes?id=4",
                    createHttpEntity(playerDTO, loginHeaders), TournamentClassDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().length, is(greaterThan(0)));

        } catch (HttpClientErrorException e) {
            fail(e.getResponseBodyAsString());
        }
    }


    @Test
    public void testImport() throws Exception {
        try {

            Long tId = createTournament("player import", 6, 2);
            TournamentDTO tournament = http(HttpMethod.GET, "api/tournaments/" + tId, createHttpEntity(null, loginHeaders), TournamentDTO.class).getBody();
            //create a class
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            int count = jdbcTemplate.queryForObject("select count(*) from T_PLAYER where TOURNAMENT_ID = " + tId, Integer.class);
            assertThat(count, is(0));
            count = jdbcTemplate.queryForObject("select count(*) from TC_PLAYER where CLASSES_ID = " + tournament.getClasses().get(0).getId(), Integer.class);
            assertThat(count, is(0));

            RestTemplate restTemplate = new RestTemplate();
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("Content-Type", "image/jpeg");
            map.add("file", new ClassPathResource("/Turnierteilnehmer.xml"));
            ResponseEntity<String> entity =
                    restTemplate.exchange("http://localhost:9999/api/players/import-from-click-tt?assignWhileImport=true", HttpMethod.POST,
                            createHttpEntity(map, loginHeaders, true), String.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody(), is("Es wurden 12 Spieler importiert"));

            count = jdbcTemplate.queryForObject("select count(*) from T_PLAYER where TOURNAMENT_ID = " + tId, Integer.class);
            assertThat(count, is(12));
            count = jdbcTemplate.queryForObject("select count(*) from TC_PLAYER where CLASSES_ID = " + tournament.getClasses().get(0).getId(), Integer.class);
            assertThat(count, is(12));

//secomnd time. no new player shall be imported
            entity =
                    restTemplate.exchange("http://localhost:9999/api/players/import-from-click-tt", HttpMethod.POST,
                            createHttpEntity(map, loginHeaders, true), String.class);
            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody(), is("Es wurden 12 Spieler importiert"));

            count = jdbcTemplate.queryForObject("select count(*) from T_PLAYER where TOURNAMENT_ID = " + tId, Integer.class);
            assertThat(count, is(12));


        } catch (HttpClientErrorException e) {
            System.err.println(e.getResponseBodyAsString());
            fail();
        }
    }
}