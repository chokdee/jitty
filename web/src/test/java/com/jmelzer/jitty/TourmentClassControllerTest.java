package com.jmelzer.jitty;

import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.TournamentClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TourmentClassControllerTest extends SecureResourceTest {
    @Test
    public void testGetTournamentClass() throws Exception {
        HttpHeaders loginHeaders = doLogin();

        ResponseEntity<TournamentClass> entity = http(HttpMethod.GET, "api/tournament-classes/2",
                createHttpEntity(null, loginHeaders), TournamentClass.class);

        assertTrue(entity.getStatusCode().is2xxSuccessful());
        assertThat(entity.getBody().getId(), is(2L));
    }

    @Test
    public void testsaveTC() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            TournamentClass tournamentClass = new TournamentClass();
            tournamentClass.setName("dummy");
            tournamentClass.setStartTTR(0);
            tournamentClass.setEndTTR(1000);

            TournamentClass result = restTemplate.postForObject("http://localhost:9999/api/tournament-classes",
                    createHttpEntity(tournamentClass, loginHeaders), TournamentClass.class);
            System.out.println("result = " + result);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

}