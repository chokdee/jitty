package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.User;
import com.jmelzer.jitty.model.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
public class UserControllerTest extends SecureResourceTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void testGetUserList() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            ResponseEntity<UserDTO[]> entity = http(HttpMethod.GET, "api/users",
                    createHttpEntity(null, loginHeaders), UserDTO[].class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody()[0].getId(), is(1L));

            assertTrue(entity.getStatusCode().is2xxSuccessful());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }
    @Test
    public void testGetOne() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

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
            HttpHeaders loginHeaders = doLogin();
            ResponseEntity<String> okResponse = restTemplate.exchange(
                    "http://localhost:9999/resource",
                    HttpMethod.GET,
                    createHttpEntity(null, loginHeaders),
                    String.class);

            assertThat(okResponse.getStatusCode(), is(HttpStatus.OK));


            User user = new User();
            user.setId(1L);
            user.setPassword("oooo");
            ResponseEntity<Void> entity = http(HttpMethod.POST, "api/users/change-password",
                    createHttpEntity(user, okResponse.getHeaders()), Void.class);

            assertTrue(entity.getStatusCode().is2xxSuccessful());

            String pw = new JdbcTemplate(dataSource).queryForObject("select password from user where id = 1", String.class);
            assertEquals("oooo", pw);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

}