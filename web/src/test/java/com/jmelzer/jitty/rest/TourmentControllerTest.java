package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.dto.TournamentDTO;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
public class TourmentControllerTest extends SecureResourceTest {

    @Test
    public void testGetList() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<TournamentDTO[]> entity = http(HttpMethod.GET, "api/tournaments",
                    createHttpEntity(null, loginHeaders), TournamentDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody()[0].getId(), is(1L));

        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    public void testGetTournament() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<TournamentDTO> entity = http(HttpMethod.GET, "api/tournaments/2",
                    createHttpEntity(null, loginHeaders), TournamentDTO.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertEquals((Long) 2L, entity.getBody().getId());
            assertThat(entity.getBody().getClasses().size(), is(greaterThan(2)));
        } catch (HttpClientErrorException e) {
            System.err.println(e.getResponseBodyAsString());
            fail();
        }

    }


    @Test
    public void saveNew() throws Exception {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {

            int size = jdbcTemplate.queryForObject("select count(*) from tournament ", Integer.class);

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

            ResponseEntity<Void> entity = http(HttpMethod.POST, "api/tournaments",
                    createHttpEntity(tournament, okResponse.getHeaders()), Void.class);

            assertThat(entity.getStatusCode(), is(HttpStatus.OK));
            assertThat(jdbcTemplate.queryForObject("select count(*) from tournament ", Integer.class), is(size + 1));

        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    public void editTournament() throws Exception {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            int tcc = jdbcTemplate.queryForObject("select count(*) from tournament_class where t_id = 2 ", Integer.class);

            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<TournamentDTO> entity = http(HttpMethod.GET, "api/tournaments/2",
                    createHttpEntity(null, loginHeaders), TournamentDTO.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            TournamentDTO dto = entity.getBody();
            assertEquals((Long) 2L, dto.getId());
            assertThat(dto.getClasses().size(), is(tcc));
            dto.setName("ControllerTest");

            ResponseEntity<Void> voidEntity = http(HttpMethod.POST, "api/tournaments",
                    createHttpEntity(dto, entity.getHeaders()), Void.class);

            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());


            assertThat(jdbcTemplate.queryForObject("select name from tournament where id = 2 ", String.class), is("ControllerTest"));
            assertThat(jdbcTemplate.queryForObject("select count(*) from tournament_class where t_id = 2 ", Integer.class), is(tcc));

        } catch (HttpClientErrorException e) {
            System.err.println(e.getResponseBodyAsString());
            fail();
        }

    }
}