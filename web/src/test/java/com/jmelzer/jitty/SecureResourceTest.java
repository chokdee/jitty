package com.jmelzer.jitty;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by J. Melzer on 14.07.2016.
 */
@WebIntegrationTest("server.port=9999")
public class SecureResourceTest {
    RestTemplate restTemplate = new TestRestTemplate();
    static final String SET_COOKIE = "Set-Cookie";
    static final String CSRF_TOKEN_HEADER = "X-CSRF-TOKEN";


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

    static HttpEntity<?> createHttpEntity(Object o, HttpHeaders headers) {
        String token = extractCsrfToken(headers);
        String jSessionId = extractJSessionId(headers);
        return createHttpEntity(o, jSessionId, token);
    }

    static HttpEntity<?> createHttpEntity(Object o, String jSessionId, String csrfToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CSRF_TOKEN_HEADER, csrfToken);
        headers.add("Cookie", String.format("JSESSIONID=%s", jSessionId));
        if (o == null) {
            return new HttpEntity<>(headers);
        } else {
            return new HttpEntity<>(o, headers);
        }
    }

    static String extractJSessionId(HttpHeaders headers) {
        String setCookieHeader = headers.get(SET_COOKIE).get(0);
        return setCookieHeader.substring(setCookieHeader.indexOf('=') + 1, setCookieHeader.indexOf(';'));
    }

    static String extractCsrfToken(HttpHeaders headers) {
        return headers.get(CSRF_TOKEN_HEADER).get(0);
    }

}
