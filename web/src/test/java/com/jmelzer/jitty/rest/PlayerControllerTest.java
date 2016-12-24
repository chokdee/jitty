package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertThat;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test player controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class PlayerControllerTest extends SecureResourceTest {

    @Test
    public void testGetList() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<TournamentPlayerDTO[]> entity = http(HttpMethod.GET, "api/players",
                    createHttpEntity(null, loginHeaders), TournamentPlayerDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody()[0].getId(), is(1L));

        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    public void testGetOne() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

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
            HttpHeaders loginHeaders = doLogin();

            TournamentPlayerDTO playerDTO = new TournamentPlayerDTO();
            ResponseEntity<TournamentClassDTO[]> entity = http(HttpMethod.POST, "api/players/possible-tournaments-classes?id=4",
                    createHttpEntity(playerDTO, loginHeaders), TournamentClassDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().length, is(greaterThan(3)));

        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }
}