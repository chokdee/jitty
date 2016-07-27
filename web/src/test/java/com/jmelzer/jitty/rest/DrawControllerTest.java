package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 26.07.2016.
 * Test draw controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DrawControllerTest extends SecureResourceTest {
    @Test
    public void getPlayerforClass() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<TournamentPlayerDTO[]> entity = http(HttpMethod.GET, "api/draw/player-for-class?cid=1",
                    createHttpEntity(null, loginHeaders), TournamentPlayerDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().length, is(greaterThan(5)));
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

}