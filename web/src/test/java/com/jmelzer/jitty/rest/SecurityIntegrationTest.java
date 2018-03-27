/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import static com.jmelzer.jitty.rest.TestUtil.*;
import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SecurityIntegrationTest extends SecureResourceTest {

    private static final TestRestTemplate anonymous = new TestRestTemplate();

    @Test
    public void thatSecuredIsNotAccessible() {
        ResponseEntity<String> response = anonymous.getForEntity("http://localhost:9999/resource", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(response.getBody(), containsString("401"));

        response = anonymous.getForEntity("http://localhost:9999/api/users/7", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(response.getBody(), containsString("401"));
    }


    // https://jira.spring.io/browse/SPR-9367
    @Test(expected = HttpClientErrorException.class)
    public void thatLoginNeedsBasicAuthentication() {
        http(HttpMethod.GET, "user", new HttpEntity<>(""), String.class);
    }

    // https://jira.spring.io/browse/SPR-9367
    @Test(expected = HttpClientErrorException.class)
    public void thatCredentialsAreChecked() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + "a:a");
        http(HttpMethod.GET, "user", new HttpEntity<>(headers), String.class);
    }


    @Test
    public void thatCsrfTokenAndSessionIsReturned() {
        ResponseEntity<String> response = http(HttpMethod.GET, "user", new HttpEntity<>(getAuthHeaders()), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        // csrf token and session
        assertThat(response.getHeaders().get(SET_COOKIE).size(), is(2));
        assertThat(response.getHeaders().get(SET_COOKIE).get(1), containsString("XSRF-TOKE"));
        // session id
        assertThat(response.getHeaders().get(SET_COOKIE).get(0), containsString("JSESSIONID"));
    }

    @Test
    public void thatSessionIsNeededToAccessSecuredResource() {
        ResponseEntity<String> response = http(HttpMethod.GET, "user", new HttpEntity<>(getAuthHeaders()), String.class);
        String token = response.getHeaders().get(SET_COOKIE).get(1);

        HttpHeaders headers = new HttpHeaders();
        headers.add(CSRF_TOKEN_HEADER, token);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        response = anonymous.exchange(
                "http://localhost:9999/api/resource",
                HttpMethod.GET,
                httpEntity,
                String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(response.getBody(), containsString("401"));
    }

    @Test
    public void thatSomeUrlsAreOpenWithoutLogin() {

        ResponseEntity<String> response = anonymous.exchange(
                "http://localhost:9999/index.html",
                HttpMethod.GET,
                new HttpEntity<>(""),
                String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void thatTokenCanBeUsedToAccessSecuredResource() {
        doLogin();

        ResponseEntity<String> response = anonymous.exchange(
                "http://localhost:9999/resource",
                HttpMethod.GET,
                createHttpEntity(loginHeaders),
                String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    private HttpEntity<?> createHttpEntity(HttpHeaders loginHeaders) {
        return TestUtil.createHttpEntity(null, loginHeaders);
    }


    @Test
    public void thatTokenCanBeUsedTwoTimesToAccessSecuredResource() {
        doLogin();

        HttpEntity<?> httpEntity = createHttpEntity(loginHeaders);

        // first time everything is ok
        ResponseEntity<String> okResponse = anonymous.exchange(
                "http://localhost:9999/resource",
                HttpMethod.GET,
                httpEntity,
                String.class);

        assertThat(okResponse.getStatusCode(), is(HttpStatus.OK));

        // second time csrf token is invalid
        ResponseEntity<String> response = anonymous.exchange(
                "http://localhost:9999/resource",
                HttpMethod.GET,
                httpEntity,
                String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));

    }

    @Test
    public void thatNewTokenCanBeUsedToAccessSecuredResource() {
        doLogin();

        // first time everything is ok
        ResponseEntity<String> okResponse = anonymous.exchange(
                "http://localhost:9999/resource",
                HttpMethod.GET,
                createHttpEntity(loginHeaders),
                String.class);

        assertThat(okResponse.getStatusCode(), is(HttpStatus.OK));

        // we can use token from first response
        ResponseEntity<String> newResponse = anonymous.exchange(
                "http://localhost:9999/resource",
                HttpMethod.GET,
                createHttpEntity(loginHeaders),
                String.class);

        assertThat(newResponse.getStatusCode(), is(HttpStatus.OK));

    }

    @Test
    public void thatSecuredResourceIsInaccessibleAfterLogout() {
        doLogin();

        // first time everything is ok
        ResponseEntity<String> okResponse = anonymous.exchange(
                "http://localhost:9999/resource",
                HttpMethod.GET,
                createHttpEntity(loginHeaders),
                String.class);

        assertThat(okResponse.getStatusCode(), is(HttpStatus.OK));

        // logout
        ResponseEntity<String> logoutResponse = anonymous.exchange(
                "http://localhost:9999/logout",
                HttpMethod.POST,
                createHttpEntity(okResponse.getHeaders()),
                String.class);

        assertThat(logoutResponse.getStatusCode(), is(HttpStatus.FOUND));

        // no csrf token
        assertNull(logoutResponse.getHeaders().get(SET_COOKIE));

        // second time csrf token is invalid
        ResponseEntity<String> response = anonymous.exchange(
                "http://localhost:9999/resource",
                HttpMethod.GET,
                createHttpEntity(loginHeaders),
                String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));

    }


}