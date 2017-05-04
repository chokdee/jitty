/*
 * Copyright (c) 2017.
 * J. Melzer
 */
package com.jmelzer.jitty.dao;

import com.jmelzer.jitty.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for {@link TournamentSingleGameRepository}.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
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
        tournamentClass.setSystemType(TournamentSystemType.GK.getValue());
        tournamentClassRepository.save(tournamentClass);
        assertNotNull(tournamentClass.getId());

        tournamentClass.createPhaseCombination(PhaseCombination.GK);
        tournamentClass.setActivePhaseNo(0);
        tournamentClass.setSystemType(TournamentSystemType.GK.getValue());
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

        TournamentSingleGame game = new TournamentSingleGame("aaa", 1L);
        game.setPlayer1(playerRepository.findOne(1L));
        game.setPlayer2(playerRepository.findOne(2L));
        repository.save(game);
        assertNotNull(game.getId());

        group.addGame(game);
        tournamentClassRepository.save(tournamentClass);


        TournamentSingleGame nextGame = new TournamentSingleGame("aaa", 1L);
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
        clz.createPhaseCombination(PhaseCombination.GK);
        clz.setActivePhaseNo(0);
        clz.setSystemType(TournamentSystemType.GK.getValue());
        TournamentGroup group = new TournamentGroup("1");
        clz.addGroup(group);

        tournamentClassRepository.save(clz);

//        calcGroupGames(clz.getGroups());
        TournamentSingleGame game = new TournamentSingleGame("222", 1L);
        game.setPlayer1(playerRepository.findOne(1L));
        game.setPlayer2(playerRepository.findOne(2L));

        game = repository.saveAndFlush(game);

        clz.getGroups().get(0).addGame(game);
        tournamentClassRepository.saveAndFlush(clz);
    }

    @Test
    @Transactional
    public void finishedGames() {

//        calcGroupGames(clz.getGroups());
        TournamentSingleGame game = new TournamentSingleGame("11", 1L);
        game.setPlayer1(playerRepository.findOne(1L));
        game.setPlayer2(playerRepository.findOne(2L));
        game.setEndTime(new Date());
        game.setPlayed(true);
        repository.save(game);

        assertThat(repository.findByPlayedAndTidOrderByEndTimeDesc(true, 1L).size(), is(1));

        TournamentSingleGame game2 = new TournamentSingleGame("222", 1L);
        game2.setPlayer1(playerRepository.findOne(1L));
        game2.setPlayer2(playerRepository.findOne(2L));
        repository.save(game2);

        assertThat(repository.findByPlayedAndTidOrderByEndTimeDesc(true, 1L).size(), is(1));

        game2.setPlayed(true);
        game2.setEndTime(new Date());

        repository.save(game2);

        List<TournamentSingleGame> list = repository.findByPlayedAndTidOrderByEndTimeDesc(true, 1L);
        assertThat(list.size(), is(2));
        assertThat(list.get(0).getId(), is(game2.getId()));

    }

}
