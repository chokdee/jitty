package com.jmelzer.jitty;

import com.jmelzer.jitty.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
@WebIntegrationTest("server.port=9999")
public class UserControllerTest {
    private RestTemplate restTemplate = new TestRestTemplate("user", "42");

    @Test
    public void testGetUserList() throws Exception {
        ResponseEntity<List<User>> entity =
                getUsers("http://localhost:9999/api/users");

        int i = 0;
//        while (i < 1000) {
//            Thread.sleep(2000);
//            i++;
//            System.out.println("i = " + i);
//        }
        assertTrue(entity.getStatusCode().is2xxSuccessful());
    }



    private ResponseEntity<List<User>> getUsers(String uri) {
        return restTemplate.exchange(
                uri, HttpMethod.GET, null, getParameterizedPageTypeReference()
        );
    }
    private ParameterizedTypeReference<List<User>> getParameterizedPageTypeReference() {
        return new ParameterizedTypeReference<List<User>>() {
        };
    }
}