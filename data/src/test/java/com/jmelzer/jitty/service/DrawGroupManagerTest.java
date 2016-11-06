package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.TournamentPlayer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by J. Melzer on 11.06.2016.
 */
public class DrawGroupManagerTest {
    DrawGroupManager drawGroupManager = new DrawGroupManager();

    @Before
    public void setup() {
        drawGroupManager.seedingManager = new SeedingManager();
    }

    @Test
    public void testcalcGroupGames() {
        TournamentService ts = Mockito.mock(TournamentService.class);
        drawGroupManager.tournamentService = ts;
        TournamentClass tc = new TournamentClass("ccc");
        TournamentGroup group = new TournamentGroup("A");
        group.setTournamentClass(tc);
        group.addPlayer(new TournamentPlayer(11L, "11", ""));
        group.addPlayer(new TournamentPlayer(12L, "12", ""));
        group.addPlayer(new TournamentPlayer(13L, "13", ""));
        group.addPlayer(new TournamentPlayer(14L, "14", ""));

        TournamentGroup group2 = new TournamentGroup("B");
        group2.setTournamentClass(tc);
        group2.addPlayer(new TournamentPlayer(15L, "15", ""));
        group2.addPlayer(new TournamentPlayer(16L, "16", ""));
        group2.addPlayer(new TournamentPlayer(17L, "17", ""));
        group2.addPlayer(new TournamentPlayer(18L, "18", ""));

        TournamentGroup group3 = new TournamentGroup("C");
        group3.setTournamentClass(tc);
        group3.addPlayer(new TournamentPlayer(19L, "19", ""));
        group3.addPlayer(new TournamentPlayer(20L, "20", ""));
        group3.addPlayer(new TournamentPlayer(21L, "21", ""));

        TournamentGroup group4 = new TournamentGroup("C");
        group4.setTournamentClass(tc);
        group4.addPlayer(new TournamentPlayer(22L, "22", ""));
        group4.addPlayer(new TournamentPlayer(23L, "23", ""));
        group4.addPlayer(new TournamentPlayer(24L, "24", ""));

        drawGroupManager.calcGroupGames(Arrays.asList(group, group2, group3, group4));
        assertThat(group.getGames().size(), is(6));
        assertThat(group2.getGames().size(), is(6));
        assertThat(group3.getGames().size(), is(3));
        assertThat(group4.getGames().size(), is(3));
    }


}