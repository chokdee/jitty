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
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.TournamentSingleGame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for {@link TournamentSingleGameRepository}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SampleDataJpaApplication.class)
public class TournamentSingleGameRepositoryIntegrationTests {

    @Autowired
    TournamentSingleGameRepository repository;

    @Autowired
    TournamentClassRepository tournamentClassRepository;

    @Autowired
    TournamentPlayerRepository playerRepository;


    @Test
    public void getAll() {
        assertThat(repository.findAll().size(), is(0));
    }

    @Test
    @Transactional
    public void testSave() {

        TournamentClass tournamentClass = new TournamentClass("test");
        tournamentClassRepository.save(tournamentClass);
        assertNotNull(tournamentClass.getId());

        TournamentGroup group = new TournamentGroup("1");
        tournamentClass.addGroup(group);

        tournamentClassRepository.save(tournamentClass);
        tournamentClass = tournamentClassRepository.findOne(tournamentClass.getId());
        assertNotNull(tournamentClass.getGroups().get(0).getId());
        group = tournamentClass.getGroups().get(0);
        assertNotNull(group.getId());

        group.addPlayer(playerRepository.findOne(1L));
        group.addPlayer(playerRepository.findOne(2L));

        tournamentClassRepository.save(tournamentClass);

        TournamentSingleGame game = new TournamentSingleGame();
        game.setPlayer1(playerRepository.findOne(1L));
        game.setPlayer2(playerRepository.findOne(2L));

        repository.save(game);
        assertNotNull(game.getId());

        group.addGame(game);
        tournamentClassRepository.save(tournamentClass);



    }

    @Test
    @Transactional
    public void nochmal() {
        TournamentClass clz = tournamentClassRepository.findOne(1L);
        TournamentGroup group = new TournamentGroup("1");
        clz.addGroup(group);

        tournamentClassRepository.save(clz);

//        calcGroupGames(clz.getGroups());
        TournamentSingleGame game = new TournamentSingleGame();
        game.setPlayer1(playerRepository.findOne(1L));
        game.setPlayer2(playerRepository.findOne(2L));

        repository.save(game);

        clz.getGroups().get(0).addGame(game);
        tournamentClassRepository.saveAndFlush(clz);
    }

}
