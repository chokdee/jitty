package com.jmelzer.jitty.rest;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.List;

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
    private String sessionId;

    static String extractCsrfToken(HttpHeaders headers) {
        List<String> list = headers.get(SET_COOKIE);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return list.get(1);
        }
    }

    @Before
    public void before() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected <T> ResponseEntity<T> http(final HttpMethod method, final String path, HttpEntity<?> entity, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange("http://localhost:9999/" + path, method, entity, responseType);
    }

    HttpHeaders doLogin() {
        return http(HttpMethod.GET, "user", new HttpEntity<>(getAuthHeaders()), String.class).getHeaders();
    }

    HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = new String(org.springframework.security.crypto.codec.Base64.encode(
                ("user" + ":" + "42").getBytes()));
        headers.set("Authorization", "Basic " + token);
        return headers;

    }

    HttpEntity<?> createHttpEntity(Object o, HttpHeaders headers) {
        String token = extractCsrfToken(headers);
        String jSessionId = extractJSessionId(headers);
        return createHttpEntity(o, jSessionId, token);
    }

    HttpEntity<?> createHttpEntity(Object o, String jSessionId, String csrfToken) {
        HttpHeaders headers = new HttpHeaders();
        String t = csrfToken.substring(csrfToken.indexOf("=") + 1);
        t = t.substring(0, t.indexOf(';'));
        headers.add(CSRF_TOKEN_HEADER, t);
        headers.add("Cookie", String.format("JSESSIONID=%s", jSessionId) + "; XSRF-TOKEN=" + t);
        if (o == null) {
            return new HttpEntity<>(headers);
        } else {
            return new HttpEntity<>(o, headers);
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

}
