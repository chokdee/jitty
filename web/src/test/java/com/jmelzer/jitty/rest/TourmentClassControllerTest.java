package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test TourmentClassController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TourmentClassControllerTest extends SecureResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testGetTournamentClass() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

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
        HttpHeaders loginHeaders = doLogin();

        thrown.expect(HttpClientErrorException.class);
        thrown.expectMessage("400 Bad Request");

        http(HttpMethod.DELETE, "api/tournament-classes/5",
                createHttpEntity(null, loginHeaders), ErrorMessage.class);

    }

    @Test
    public void getNotRunning() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<TournamentClassDTO[]> entity = http(HttpMethod.GET, "api/tournament-classes/not-running",
                    createHttpEntity(null, loginHeaders), TournamentClassDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().length, is(greaterThan(2)));
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    public void testSaveNewTC() throws Exception {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            assertThat(jdbcTemplate.queryForObject("select count(*) from tournament_class where t_id = 2", Integer.class), is(5));

            HttpHeaders loginHeaders = doLogin();
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

            ResponseEntity<Void> entity = http(HttpMethod.POST, "api/tournament-classes/2",
                    createHttpEntity(tournamentClass, okResponse.getHeaders()), Void.class);

            assertThat(entity.getStatusCode(), is(HttpStatus.OK));

            assertThat(jdbcTemplate.queryForObject("select count(*) from tournament_class where t_id = 2", Integer.class), is(6));
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

}