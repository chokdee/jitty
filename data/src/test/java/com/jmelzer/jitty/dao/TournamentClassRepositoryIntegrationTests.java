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
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for {@link UserRepository}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SampleDataJpaApplication.class)
public class TournamentClassRepositoryIntegrationTests {

    @Autowired
    TournamentClassRepository repository;

    @Autowired
    TournamentRepository tournamentRepository;


    @Test
    public void getAll() {
        assertThat(repository.findAll().size(), greaterThan(2));
    }

    @Test
    public void findByTournament() {
        Tournament tournament = tournamentRepository.findOne(2L);
        assertNotNull(tournament);
        assertThat(repository.findByTournament(tournament).size(), greaterThan(2));
    }


    @Test
    public void findByTournamentAndEndTTR() {
        Tournament tournament = tournamentRepository.findOne(2L);
        assertNotNull(tournament);
        assertThat(repository.findByTournamentAndEndTTRGreaterThanAndStartTTRLessThan(tournament, 1600, 1600).size(), is(1));
        assertThat(repository.findByTournamentAndEndTTRGreaterThanAndStartTTRLessThan(tournament, 1599, 1599).size(), is(2));
        assertThat(repository.findByTournamentAndEndTTRGreaterThanAndStartTTRLessThan(tournament, 3500, 1).size(), is(0));
        assertThat(repository.findByTournamentAndEndTTRGreaterThanAndStartTTRLessThan(tournament, 2000, 2000).size(), is(1));
    }

    @Test
    public void findByTournamentAndRunning() {
        Tournament tournament = tournamentRepository.findOne(2L);
        assertNotNull(tournament);
        assertThat(repository.findByTournamentAndRunning(tournament, false).size(), is(3));
        assertThat(repository.findByTournamentAndRunning(tournament, true).size(), is(0));
    }

    @Transactional
    @Test
    public void saveWithGroup() {
        TournamentClass clz = repository.findOne(1L);
        assertNotNull(clz);

        clz.addGroup(new TournamentGroup("1"));
        clz.addGroup(new TournamentGroup("2"));

        repository.save(clz);

        assertThat(repository.findOne(1L).getGroups().size(), is(2));
    }
}
