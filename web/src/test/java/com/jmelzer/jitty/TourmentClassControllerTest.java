package com.jmelzer.jitty;

import com.jmelzer.jitty.model.TournamentClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
@WebIntegrationTest("server.port=9999")
public class TourmentClassControllerTest {
    private RestTemplate restTemplate = new TestRestTemplate("user", "42");

    @Test
    public void testGetTournament() throws Exception {
        ResponseEntity<String> result = restTemplate.exchange("http://localhost:9999/api/tournament-classes/2",
                HttpMethod.GET, null, String.class);
        System.out.println(result.getBody());
        assertEquals(result.getBody(), 200, result.getStatusCode().value());

        ResponseEntity<TournamentClass> entity =
                getTournamentClass("http://localhost:9999/api/tournament-classes/2");

        assertTrue(entity.getStatusCode().is2xxSuccessful());
    }



    private ResponseEntity<TournamentClass> getTournamentClass(String uri) {
        return restTemplate.exchange(
                uri, HttpMethod.GET, null,getParameterizedPageTypeReferenceT()
        );
    }
    private ParameterizedTypeReference<TournamentClass> getParameterizedPageTypeReferenceT() {
        return new ParameterizedTypeReference<TournamentClass>() {
        };
    }
}