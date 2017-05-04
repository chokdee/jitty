/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.TournamentSystemType;
import com.jmelzer.jitty.model.dto.GroupPhaseDTO;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 28.04.2017.
 */
public class TestUtil {
    static final String SET_COOKIE = "Set-Cookie";

    static final String CSRF_TOKEN_HEADER = "X-XSRF-TOKEN";

    public static HttpHeaders loginHeaders;

    private static String sessionId;

    public static Long createTournament(String name, int tableCount, int type) {
        //create a tournament
        TournamentDTO tournament = new TournamentDTO();
        tournament.setName(name);
        tournament.setTableCount(tableCount);
        tournament.setStartDate(new Date());
        tournament.setEndDate(new Date());
        tournament.setType(type);
        ResponseEntity<Long> longEntitiy = http(HttpMethod.POST, "api/tournaments", createHttpEntity(tournament, loginHeaders), Long.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
        Long tId = longEntitiy.getBody();

        http(HttpMethod.GET, "api/tournaments/actual/" + tId, createHttpEntity(tournament, loginHeaders), Void.class);
        return tId;
    }

    public static <T> ResponseEntity<T> http(final HttpMethod method, final String path, HttpEntity<?> entity, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(method + ":" + "http://localhost:9999/" + path);
        return restTemplate.exchange("http://localhost:9999/" + path, method, entity, responseType);
    }

    public static HttpEntity<?> createHttpEntity(Object o, HttpHeaders headers) {
        return createHttpEntity(o, headers, false);
    }

    public static HttpEntity<?> createHttpEntity(Object o, HttpHeaders headers, boolean multiform) {
        String token = extractCsrfToken(headers);
        String jSessionId = extractJSessionId(headers);
        return createHttpEntity(o, jSessionId, token, multiform);
    }

    public static String extractCsrfToken(HttpHeaders headers) {
        List<String> list = headers.get(SET_COOKIE);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return list.get(1);
        }
    }

    public static String extractJSessionId(HttpHeaders headers) {
        if (sessionId != null) {
            return sessionId;
        }
        String setCookieHeader = headers.get(SET_COOKIE).get(0);
        sessionId = setCookieHeader.substring(setCookieHeader.indexOf('=') + 1, setCookieHeader.indexOf(';'));
        return sessionId;
    }

    public static HttpEntity<?> createHttpEntity(Object o, String jSessionId, String csrfToken, boolean multiform) {
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

    public static TournamentClassDTO createClz(Long tId, String name, TournamentSystemType systemType) {
        TournamentClassDTO tournamentClass = new TournamentClassDTO();
        tournamentClass.setName(name);
        tournamentClass.setStartTTR(0);
        tournamentClass.setEndTTR(3000);
        tournamentClass.setSystemType(systemType.getValue());

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

    public static void createPlayer(int i, TournamentClassDTO tournamentClass) {
        TournamentPlayerDTO player = new TournamentPlayerDTO();
        player.setFirstName(i + " aaaa");
        player.setLastName(i + " bbbb");
        player.setQttr(1660 + i);
        player.addClass(tournamentClass);
        ResponseEntity<Void> longEntitiy = http(HttpMethod.POST, "api/players",
                createHttpEntity(player, loginHeaders), Void.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
    }

    public static void doLogin() {
        loginHeaders = http(HttpMethod.GET, "user", new HttpEntity<>(getAuthHeaders()), String.class).getHeaders();
    }

    public static HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = new String(org.springframework.security.crypto.codec.Base64.encode(
                ("user" + ":" + "42").getBytes()));
        headers.set("Authorization", "Basic " + token);
        return headers;

    }

    public static void reset() {
        sessionId = null;
        loginHeaders = null;
    }
}
