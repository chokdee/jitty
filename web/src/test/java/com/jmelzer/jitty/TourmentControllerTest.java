package com.jmelzer.jitty;

import com.jmelzer.jitty.model.Tournament;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
public class TourmentControllerTest extends SecureResourceTest {

    @Test
    public void testGetTournament() throws Exception {
        HttpHeaders loginHeaders = doLogin();

        ResponseEntity<Tournament> entity = http(HttpMethod.GET, "api/tournaments/2",
                createHttpEntity(null, loginHeaders), Tournament.class);

        System.out.println();
        System.out.println("-----------------");
        System.out.println("entity.getBody() = " + entity.getBody());
        System.out.println("-----------------");
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        assertEquals((Long) 2L, entity.getBody().getId());
    }


    @Test
    public void testsave() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();
            Tournament tournament = new Tournament();
            tournament.setName("dummy");
            tournament.setStartDate(new Date());
            tournament.setEndDate(new Date());
            Map<String, String> vars = new HashMap<String, String>();
            String uri = "http://localhost:9999/api/tournaments";
            ResponseEntity<Tournament> ex = http(HttpMethod.POST, "api/tournaments",
                    createHttpEntity(tournament, loginHeaders), Tournament.class);
            Tournament result = ex.getBody();
            System.out.println();
            System.out.println("-----------------");
            System.out.println("result = " + result);
            System.out.println("-----------------");
            assertNotNull(result.getId());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }
}