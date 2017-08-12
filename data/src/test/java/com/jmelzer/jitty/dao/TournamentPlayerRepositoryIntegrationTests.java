/*
 * Copyright (c) 2017.
 * J. Melzer
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

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

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
        TournamentPlayer p = repository.findByLastName("Boll");
        assertEquals("Boll", repository.findByLastNameAndFirstNameAndTournament("Boll", "Timo",
                tournamentRepository.findOne(2L)).get(0).getLastName());
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
