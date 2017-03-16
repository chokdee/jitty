package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.dto.GroupPhaseDTO;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentDTO;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 14.07.2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=9999"})
public abstract class SecureResourceTest {
    static final String SET_COOKIE = "Set-Cookie";

    static final String CSRF_TOKEN_HEADER = "X-XSRF-TOKEN";

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbcTemplate;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders loginHeaders;

    private String sessionId;

    @Before
    public void before() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    HttpHeaders doLogin() {
        return http(HttpMethod.GET, "user", new HttpEntity<>(getAuthHeaders()), String.class).getHeaders();
    }

    protected <T> ResponseEntity<T> http(final HttpMethod method, final String path, HttpEntity<?> entity, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(method + ":" + "http://localhost:9999/" + path);
        return restTemplate.exchange("http://localhost:9999/" + path, method, entity, responseType);
    }

    HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = new String(org.springframework.security.crypto.codec.Base64.encode(
                ("user" + ":" + "42").getBytes()));
        headers.set("Authorization", "Basic " + token);
        return headers;

    }

    protected Long createTournament(String name) {
        //create a tournament
        TournamentDTO tournament = new TournamentDTO();
        tournament.setName(name);
        tournament.setTableCount(6);
        tournament.setStartDate(new Date());
        tournament.setEndDate(new Date());
        ResponseEntity<Long> longEntitiy = http(HttpMethod.POST, "api/tournaments",
                createHttpEntity(tournament, loginHeaders), Long.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
        Long tId = longEntitiy.getBody();

        http(HttpMethod.GET, "api/tournaments/actual/" + tId, createHttpEntity(tournament, loginHeaders), Void.class);
        return tId;
    }

    HttpEntity<?> createHttpEntity(Object o, HttpHeaders headers) {
        return createHttpEntity(o, headers, false);
    }

    HttpEntity<?> createHttpEntity(Object o, HttpHeaders headers, boolean multiform) {
        String token = extractCsrfToken(headers);
        String jSessionId = extractJSessionId(headers);
        return createHttpEntity(o, jSessionId, token, multiform);
    }

    static String extractCsrfToken(HttpHeaders headers) {
        List<String> list = headers.get(SET_COOKIE);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return list.get(1);
        }
    }

    String extractJSessionId(HttpHeaders headers) {
        if (sessionId != null) {
            return sessionId;
        }
        String setCookieHeader = headers.get(SET_COOKIE).get(0);
        sessionId = setCookieHeader.substring(setCookieHeader.indexOf('=') + 1, setCookieHeader.indexOf(';'));
        return sessionId;
    }

    HttpEntity<?> createHttpEntity(Object o, String jSessionId, String csrfToken, boolean multiform) {
        HttpHeaders headers = new HttpHeaders();
        String t = csrfToken.substring(csrfToken.indexOf("=") + 1);
        t = t.substring(0, t.indexOf(';'));
        headers.add(CSRF_TOKEN_HEADER, t);
        if (multiform) {
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        }
        headers.add("Cookie", String.format("JSESSIONID=%s", jSessionId) + "; XSRF-TOKEN=" + t);
        if (o == null) {
            return new HttpEntity<>(headers);
        } else {
            return new HttpEntity<>(o, headers);
        }
    }

    protected TournamentClassDTO createClz(Long tId, String name) {
        TournamentClassDTO tournamentClass = new TournamentClassDTO();
        tournamentClass.setName(name);
        tournamentClass.setStartTTR(0);
        tournamentClass.setEndTTR(3000);

        ResponseEntity<Long> longEntitiy = http(HttpMethod.POST, "api/tournament-classes/" + tId,
                createHttpEntity(tournamentClass, loginHeaders), Long.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
        Long tClassId = longEntitiy.getBody();
        assertNotNull(tClassId);

        ResponseEntity<GroupPhaseDTO> phaseDTO = http(HttpMethod.GET, "api/draw/actual-phase?cid=" + tClassId,
                createHttpEntity(null, loginHeaders), GroupPhaseDTO.class);

        assertNull("still not drawed", phaseDTO.getBody());
        //return fresh copy
        return http(HttpMethod.GET, "api/tournament-classes/" + tClassId,
                createHttpEntity(null, loginHeaders), TournamentClassDTO.class).getBody();
    }
}
