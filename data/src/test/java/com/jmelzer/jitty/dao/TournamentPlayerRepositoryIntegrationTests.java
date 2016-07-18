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
import com.jmelzer.jitty.model.Gender;
import com.jmelzer.jitty.model.TournamentPlayer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Integration tests for {@link UserRepository}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SampleDataJpaApplication.class)
public class TournamentPlayerRepositoryIntegrationTests {

    @Autowired
    TournamentPlayerRepository repository;

    @Test
    public void findsFirstPageOf() {

        List<TournamentPlayer> players = repository.findAll();
        assertThat(players.size(), is(greaterThan(2)));
    }

    @Test
    public void findByName() {

        assertNull(repository.findByLastName("xxx"));
        assertEquals("Meier", repository.findByLastName("Meier").getLastName());
    }

    @Test
    public void save() {
        TournamentPlayer player = new TournamentPlayer();
        player.setBirthday(new Date(99, 1, 1));
        player.setEmail("blub@blub.de");
        player.setFirstName("Macy");
        player.setLastName("Stacey");
        player.setAssociation("WTTV");
        player.setClubName("Bla Club");
        player.setGender(Gender.W.toString());
        player.setMobileNumber("0800-26662662");
        player.setQttr(2000);
        player.setTtr(2100);
        repository.save(player);

        assertNotNull(player.getId());
    }
}
