package com.jmelzer.jitty;

import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.User;
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

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
@WebIntegrationTest("server.port=9999")
public class TourmentControllerTest {
    private RestTemplate restTemplate = new TestRestTemplate("user", "42");

    @Test
    public void testGetTournament() throws Exception {
        ResponseEntity<Tournament> entity =
                getTournament("http://localhost:9999/api/tournaments/2");

        assertTrue(entity.getStatusCode().is2xxSuccessful());
    }



    private ResponseEntity<List<Tournament>> getTournaments(String uri) {
        return restTemplate.exchange(
                uri, HttpMethod.GET, null,getParameterizedPageTypeReference()
        );
    }
    private ParameterizedTypeReference<List<Tournament>> getParameterizedPageTypeReference() {
        return new ParameterizedTypeReference<List<Tournament>>() {
        };
    }

    private ResponseEntity<Tournament> getTournament(String uri) {
        return restTemplate.exchange(
                uri, HttpMethod.GET, null,getParameterizedPageTypeReferenceT()
        );
    }
    private ParameterizedTypeReference<Tournament> getParameterizedPageTypeReferenceT() {
        return new ParameterizedTypeReference<Tournament>() {
        };
    }
}