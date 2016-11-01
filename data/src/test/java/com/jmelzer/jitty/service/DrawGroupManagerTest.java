package com.jmelzer.jitty.service;

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

    List<String[]> names() {
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"Ma", "Long (1)"});
        list.add(new String[]{"Fan", "Zhedong (2)"});
        list.add(new String[]{"Xu", "Xin (3)"});
        list.add(new String[]{"Zhang", "Jike (4)"});
        list.add(new String[]{"Jun", "Mizutani (5)"});
        list.add(new String[]{"Chih-Yua", "Chuang (6)"});
        list.add(new String[]{"Chun Ting", "Wong (7)"});
        list.add(new String[]{"Vladimir", "Samsonov (8)"});
        list.add(new String[]{"Fang", "Bo (9)"});
        list.add(new String[]{"Marcos", "Freitas (10)"});
        list.add(new String[]{"Tang", "Peng (11)"});
        list.add(new String[]{"Sangsu", "Lee (12)"});
        list.add(new String[]{"Maharu", "Yoshimura (13)"});
        list.add(new String[]{"Youngsik", "Jeoung (14)"});
        list.add(new String[]{"Koki", "Niwa (15)"});
        list.add(new String[]{"Tiago", "Apolonia (16)"});
        return list;
    }


    @Test
    public void testcalcGroupGames() {
        TournamentService ts = Mockito.mock(TournamentService.class);
        drawGroupManager.tournamentService = ts;
        TournamentGroup group = new TournamentGroup("A");
        group.addPlayer(new TournamentPlayer(11L, "11", ""));
        group.addPlayer(new TournamentPlayer(12L, "12", ""));
        group.addPlayer(new TournamentPlayer(13L, "13", ""));
        group.addPlayer(new TournamentPlayer(14L, "14", ""));

        TournamentGroup group2 = new TournamentGroup("B");
        group2.addPlayer(new TournamentPlayer(15L, "15", ""));
        group2.addPlayer(new TournamentPlayer(16L, "16", ""));
        group2.addPlayer(new TournamentPlayer(17L, "17", ""));
        group2.addPlayer(new TournamentPlayer(18L, "18", ""));

        TournamentGroup group3 = new TournamentGroup("C");
        group3.addPlayer(new TournamentPlayer(19L, "19", ""));
        group3.addPlayer(new TournamentPlayer(20L, "20", ""));
        group3.addPlayer(new TournamentPlayer(21L, "21", ""));

        TournamentGroup group4 = new TournamentGroup("C");
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