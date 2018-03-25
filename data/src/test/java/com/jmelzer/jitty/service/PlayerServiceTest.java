/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.AssociationRepository;
import com.jmelzer.jitty.dao.ClubRepository;
import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.model.Club;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.TournamentPlayer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by J. Melzer
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {
    @Mock
    TournamentPlayerRepository playerRepository;

    @Mock
    ClubRepository clubRepository;

    @InjectMocks
    PlayerService playerService = new PlayerService();

    @Mock
    TournamentRepository tournamentRepository;

    @Mock
    AssociationRepository associationRepository;

    @Before
    public void setup() {
    }

    @Test
    public void importPlayerFromClickTT() throws Exception {
        playerService.xmlImporter = new XMLImporter();
        Tournament t = new Tournament();
        t.setId(1L);
        when(tournamentRepository.getOne(1L)).thenReturn(t);
        InputStream inputStream = getClass().getResourceAsStream("/xml-import/Turnierteilnehmer.xml");

        TournamentPlayer tp = new TournamentPlayer(2L, "Gerd", "Bosse");
        tp.setClub(new Club("TTV BW Neudorf"));
        when(playerRepository.findByLastNameAndFirstNameAndTournament("Bosse", "Gerd", t)).thenReturn(Collections.singletonList(tp));

        TournamentPlayer tp2 = new TournamentPlayer(3L, "Mario", "Dauth");
        tp2.setClub(new Club("Falscger Verein"));
        when(playerRepository.findByLastNameAndFirstNameAndTournament("Dauth", "Mario", t)).thenReturn(Collections.singletonList(tp2));
        when(playerRepository.saveAndFlush(anyObject())).thenReturn(tp2);

        when(associationRepository.findByShortNameIgnoreCase(anyObject())).thenReturn(null);

        playerService.importPlayerFromClickTT(inputStream, 1L, false);

        Mockito.verify(playerRepository, times(1)).saveAndFlush(tp);
        Mockito.verify(playerRepository, times(12)).saveAndFlush(anyObject());
        assertThat(tp.getQttr(), is(1219));
        assertThat(tp.getBirthday(), is(new Date(54, 0, 1)));
    }

}