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

import com.jmelzer.jitty.model.Gender;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentPlayer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Integration tests for {@link UserRepository}.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TournamentPlayerRepositoryIntegrationTests {

    @Autowired
    TournamentPlayerRepository repository;

    @Autowired
    AssociationRepository associationRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    TournamentClassRepository tournamentClassRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Test
    public void findsFirstPageOf() {

        List<TournamentPlayer> players = repository.findAll();
        assertThat(players.size(), is(greaterThan(2)));
    }

    @Test
    public void findByName() {

        assertNull(repository.findByLastName("xxx"));
        assertEquals("Boll", repository.findByLastName("Boll").getLastName());
        assertEquals("Boll", repository.findByLastNameAndFirstName("Boll", "Timo").get(0).getLastName());
    }

    @Test
    @Transactional
    public void save() {
        TournamentPlayer player = new TournamentPlayer();
        player.setBirthday(new GregorianCalendar(1980, 2, 11).getTime());
        player.setEmail("blub@blub.de");
        player.setFirstName("Macy");
        player.setLastName("Stacey");
        player.setGender(Gender.W.toString());
        player.setMobileNumber("0800-26662662");
        player.setQttr(2000);
        player.setTtr(2100);
        player.setTournament(tournamentRepository.findOne(2L));
        player.setAssociation(associationRepository.findOne(1L));
        player.setClub(clubRepository.findOne(1L));
        repository.saveAndFlush(player);

        assertNotNull(player.getId());

        TournamentClass clz = tournamentClassRepository.findAll().get(0);
        Tournament t = clz.getTournament();

        assertThat(tournamentClassRepository.findAll().size(), is(3));
        assertThat(tournamentClassRepository.findByTournamentAndEndTTRGreaterThanAndStartTTRLessThan(t, 1000, 1000).size(), is(2));

        player.addClass(clz);
        repository.saveAndFlush(player);

        assertThat(repository.findOne(player.getId()).getClasses().size(), is(1));
        assertThat(tournamentClassRepository.findAll().size(), is(3));
        assertThat(tournamentClassRepository.findByTournamentAndEndTTRGreaterThanAndStartTTRLessThan(t, 1000, 1000).size(), is(2));
    }

    @Test
    public void findByClasses() {
        TournamentClass tc = tournamentClassRepository.findOne(1L);
        assertThat(repository.findByClasses(Arrays.asList(tc)).size(), is(1));
    }
}
