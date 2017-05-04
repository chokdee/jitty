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

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for {@link UserRepository}.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
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
    public void saveWithPhase() {
        TournamentClass clz = new TournamentClass("saveWithPhase");

        clz.createPhaseCombination(PhaseCombination.GK);
        clz.setSystemType(TournamentSystemType.GK.getValue());
        repository.save(clz);

        clz.addGroup(new TournamentGroup("1"));
        clz.addGroup(new TournamentGroup("2"));

        clz = repository.findOne(clz.getId());
        assertThat(clz.getSystem().getPhases().size(), is(2));
    }
}
