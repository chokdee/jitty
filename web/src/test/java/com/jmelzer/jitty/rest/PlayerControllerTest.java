/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.junit.Before;
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


    @Before
    public void before() {
        doLogin();
    }
    @Test
    public void testGetList() throws Exception {
        try {

            ResponseEntity<TournamentPlayerDTO[]> entity = http(HttpMethod.GET, "api/players",
                    createHttpEntity(null, loginHeaders), TournamentPlayerDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody()[0].getId(), is(greaterThan(1L)));

        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
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
            System.out.println(e.getResponseBodyAsString());
            fail();
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
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }


    @Test
    public void testImport() throws Exception {
        try {

            Long tId = createTournament("player import", 6, 1);
            //create a class
            TournamentClassDTO tournamentClass = createClz(tId, "Damen/Herren");
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            int count = jdbcTemplate.queryForObject("select count(*) from tournament_player", Integer.class);

            RestTemplate restTemplate = new RestTemplate();
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("Content-Type", "image/jpeg");
            map.add("file", new ClassPathResource("/Turnierteilnehmer.xml"));
            ResponseEntity<String> entity =
                    restTemplate.exchange("http://localhost:9999/api/players/import-from-click-tt", HttpMethod.POST,
                            createHttpEntity(map, loginHeaders, true) , String.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody(), is("Es wurden 12 Spieler importiert"));

            assertEquals(count + 12 ,
                    (int)jdbcTemplate.queryForObject("select count(*) from tournament_player", Integer.class));
//secomnd time. no new player shall be imported
            entity =
                    restTemplate.exchange("http://localhost:9999/api/players/import-from-click-tt", HttpMethod.POST,
                            createHttpEntity(map, loginHeaders, true) , String.class);
            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody(), is("Es wurden 12 Spieler importiert"));

            assertEquals(count + 12 ,
                    (int)jdbcTemplate.queryForObject("select count(*) from tournament_player", Integer.class));


        } catch (HttpClientErrorException e) {
            System.err.println(e.getResponseBodyAsString());
            fail();
        }
    }
}