package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.junit.Ignore;
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

    @Test
    public void calcOptimalGroupSize() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<TournamentClassDTO> entity = http(HttpMethod.GET, "api/tournament-classes/1",
                    createHttpEntity(null, loginHeaders), TournamentClassDTO.class);

            assertNull(entity.getBody().getGroupCount());

            entity = http(HttpMethod.POST, "api/draw/calc-optimal-group-size",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().getGroupCount(), is(greaterThan(1)));
//            System.out.println("entity = " + entity.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    @Ignore
    public void automaticDraw() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<TournamentClassDTO> entity = http(HttpMethod.GET, "api/tournament-classes/1",
                    createHttpEntity(null, loginHeaders), TournamentClassDTO.class);

            assertNull(entity.getBody().getGroupCount());

            entity = http(HttpMethod.POST, "api/draw/calc-optimal-group-size",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());


            entity = http(HttpMethod.POST, "api/draw/automatic-draw",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);

            System.out.println("entity = " + entity.getBody());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    public void automaticDrawAndsave() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<TournamentClassDTO> entity = http(HttpMethod.GET, "api/tournament-classes/1",
                    createHttpEntity(null, loginHeaders), TournamentClassDTO.class);

            assertNull(entity.getBody().getGroupCount());

            entity = http(HttpMethod.POST, "api/draw/calc-optimal-group-size",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());


            entity = http(HttpMethod.POST, "api/draw/automatic-draw",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());

            ResponseEntity<Void> voidEntity = http(HttpMethod.POST, "api/draw/save",
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());


        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

}