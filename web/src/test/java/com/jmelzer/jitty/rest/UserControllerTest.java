/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.User;
import com.jmelzer.jitty.model.dto.UserDTO;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;

import static com.jmelzer.jitty.rest.TestUtil.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
public class UserControllerTest extends SecureResourceTest {

    @Test
    public void testGetUserList() throws Exception {
        try {
            doLogin();

            ResponseEntity<UserDTO[]> entity = http(HttpMethod.GET, "api/users",
                    createHttpEntity(null, loginHeaders), UserDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody()[0].getId(), is(1L));

            assertTrue(entity.getStatusCode().is2xxSuccessful());
        } catch (HttpClientErrorException e) {
            fail(e.getResponseBodyAsString());
        }
    }

    @Test
    public void testNewUser() throws Exception {
        try {
            doLogin();
            ResponseEntity<String> okResponse = restTemplate.exchange(
                    "http://localhost:9999/resource",
                    HttpMethod.GET,
                    createHttpEntity(null, loginHeaders),
                    String.class);

            UserDTO user = new UserDTO();
            user.setEmail("bla@bla.de");
            user.setLoginName("bla");
            user.setName("bla");
            user.setPassword("blub");
            ResponseEntity<Void> entity = http(HttpMethod.POST, "api/users",
                    createHttpEntity(user, okResponse.getHeaders()), Void.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());

            ResponseEntity<UserDTO[]> entityList = http(HttpMethod.GET, "api/users",
                    createHttpEntity(null, loginHeaders), UserDTO[].class);
            boolean found = false;
            for (UserDTO userDTO : entityList.getBody()) {
                if (userDTO.getEmail().equals(user.getEmail())) {
                    found = true;
                }
            }
            assertTrue(found);

        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    public void testGetOne() throws Exception {
        try {
            doLogin();

            ResponseEntity<UserDTO> entity = http(HttpMethod.GET, "api/users/1",
                    createHttpEntity(null, loginHeaders), UserDTO.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().getId(), is(1L));

        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    @Test
    public void testChangePw() throws Exception {
        try {
            doLogin();
            ResponseEntity<String> okResponse = restTemplate.exchange(
                    "http://localhost:9999/resource",
                    HttpMethod.GET,
                    createHttpEntity(null, loginHeaders),
                    String.class);

            assertThat(okResponse.getStatusCode(), is(HttpStatus.OK));


            User user = new User();
            user.setId(2L);
            user.setPassword("oooo");
            ResponseEntity<Void> entity = http(HttpMethod.POST, "api/users/change-password",
                    createHttpEntity(user, okResponse.getHeaders()), Void.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());

            String pw = new JdbcTemplate(dataSource).queryForObject("select password from user where id = 2", String.class);
            assertEquals("oooo", pw);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

}