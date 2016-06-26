/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jmelzer.jitty.dao;

import com.jmelzer.jitty.SampleDataJpaApplication;
import com.jmelzer.jitty.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Integration tests for {@link UserRepository}.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SampleDataJpaApplication.class)
public class UserRepositoryIntegrationTests {

    @Autowired
    UserRepository repository;

    @Test
    public void findsFirstPageOfUser() {

        Page<User> users = repository.findAll(new PageRequest(1,100));
        assertThat(users.getTotalElements(), is(greaterThan(2L)));
    }

    @Test
    public void findByName() {

        assertNull(repository.findByName("xxx"));
        assertEquals("adminname", repository.findByName("adminname").getName());
    }

    @Test
    public void findByLoginName() {

        assertNull(repository.findByLoginName("xxx"));
        assertEquals("username", repository.findByLoginName("user").getName());
    }
    @Test
    public void save() {

        User user = new User();
        user.setLoginName("lll");
        user.setName("nnn");
        user.setPassword("ppp");
        user.setEmail("eee");
        repository.save(user);
        assertNotNull(user.getId());
    }
}
