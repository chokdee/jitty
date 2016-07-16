package com.jmelzer.jitty;

import com.jmelzer.jitty.model.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 19.05.2016.
 * Test user controller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
public class UserControllerTest extends SecureResourceTest {

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

}