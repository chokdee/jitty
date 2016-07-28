package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.dto.TournamentDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertThat;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
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
    public void testsave() throws Exception {

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

}