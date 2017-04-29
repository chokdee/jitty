/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static com.jmelzer.jitty.rest.TestUtil.doLogin;
import static com.jmelzer.jitty.rest.TestUtil.reset;

/**
 * Created by J. Melzer on 14.07.2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "server.port=9999"})
public abstract class SecureResourceTest {


    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbcTemplate;

    TestRestTemplate restTemplate = new TestRestTemplate();



    @Before
    public void before() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        doLogin();
    }

    @After
    public void after() {
        reset();
    }






}
