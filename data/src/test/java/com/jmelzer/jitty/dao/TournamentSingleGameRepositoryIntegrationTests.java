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

import java.util.Date;
import java.util.List;

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
        game.setTcName("aaa");
        repository.save(game);
        assertNotNull(game.getId());

        group.addGame(game);
        tournamentClassRepository.save(tournamentClass);


        TournamentSingleGame nextGame = new TournamentSingleGame();
        nextGame.setTcName("aaa");
        game.setNextGame(nextGame);
        repository.save(nextGame);
        repository.save(game);
        game = repository.findOne(game.getId());
        game.getNextGame().setPlayer1(playerRepository.findOne(1L));
        repository.save(game.getNextGame());
        game = repository.findOne(game.getId());
        assertNotNull(game.getNextGame().getPlayer1());


    }

    @Test
    @Transactional
    public void nochmal() {
        TournamentClass clz = tournamentClassRepository.findOne(1L);
        TournamentGroup group = new TournamentGroup("1");
        clz.addGroup(group);

        tournamentClassRepository.save(clz);

//        calcGroupGames(clz.getGroups());
        TournamentSingleGame game = new TournamentSingleGame("222");
        game.setPlayer1(playerRepository.findOne(1L));
        game.setPlayer2(playerRepository.findOne(2L));

        repository.save(game);

        clz.getGroups().get(0).addGame(game);
        tournamentClassRepository.saveAndFlush(clz);
    }

    @Test
    @Transactional
    public void finishedGames() {

//        calcGroupGames(clz.getGroups());
        TournamentSingleGame game = new TournamentSingleGame("11");
        game.setPlayer1(playerRepository.findOne(1L));
        game.setPlayer2(playerRepository.findOne(2L));
        game.setEndTime(new Date());
        game.setPlayed(true);
        repository.save(game);

        assertThat(repository.findByPlayedOrderByEndTimeDesc(true).size(), is(1));

        TournamentSingleGame game2 = new TournamentSingleGame("222");
        game2.setPlayer1(playerRepository.findOne(1L));
        game2.setPlayer2(playerRepository.findOne(2L));
        repository.save(game2);

        assertThat(repository.findByPlayedOrderByEndTimeDesc(true).size(), is(1));

        game2.setPlayed(true);
        game2.setEndTime(new Date());

        repository.save(game2);

        List<TournamentSingleGame> list =  repository.findByPlayedOrderByEndTimeDesc(true);
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getId(), is(game2.getId()));

    }

}
