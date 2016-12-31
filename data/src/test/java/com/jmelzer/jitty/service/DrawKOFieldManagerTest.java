package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by J. Melzer on 11.06.2016.
 */
public class DrawKOFieldManagerTest {
    DrawKoFieldManager drawKoFieldManager = new DrawKoFieldManager();

    @Before
    public void setup() {
        drawKoFieldManager.seedingManager = new SeedingManager();
    }

    @Test
    public void createKOField() {
        KOField field = drawKoFieldManager.createKOField(RoundType.R64);
        assertNotNull(field.getRound().getNextRound().getNextRound().getNextRound().getNextRound().getNextRound());
    }

    @Test
    public void assignPlayerToKoField32() {

        List<TournamentGroup> groups = createPlayerForKOField(30);

        KOField field = drawKoFieldManager.createKOField(RoundType.R32);
        List<TournamentSingleGame> games = drawKoFieldManager.assignPlayerToKoField(field, groups);

        assertEquals(1, games.get(0).getPlayer1().ranking);
        assertEquals(2, games.get(15).getPlayer2().ranking);
        assertEquals(3, games.get(8).getPlayer1().ranking);
        assertEquals(4, games.get(7).getPlayer2().ranking);
        assertEquals(5, games.get(4).getPlayer1().ranking);
        assertEquals(6, games.get(11).getPlayer2().ranking);
        assertEquals(7, games.get(12).getPlayer1().ranking);
        assertEquals(8, games.get(3).getPlayer2().ranking);
//        assertEquals(9, players[5].ranking);
//        assertEquals(10, players[28].ranking);
//        assertEquals(11, players[21].ranking);
//        assertEquals(12, players[12].ranking);
//        assertEquals(13, players[13].ranking);
//        assertEquals(14, players[19].ranking);
//        assertEquals(15, players[30].ranking);
        //15 player are set only
//        assertEquals(16, players[4].ranking);
    }

    @Test
    public void assignPlayerToKoField16() {

        List<TournamentGroup> groups = createPlayerForKOField(16);

        KOField field = drawKoFieldManager.createKOField(RoundType.R16);
        List<TournamentSingleGame> games = drawKoFieldManager.assignPlayerToKoField(field, groups);
        assertEquals("Ma", games.get(0).getPlayer1().getFirstName());
        assertEquals("Fan", games.get(7).getPlayer2().getFirstName());
        //todo etc

        for (TournamentSingleGame game : games) {
            assertNotNull("all player must be filled", game.getPlayer1());
            assertNotNull("all player must be filled", game.getPlayer2());
        }
        assertThat(field.getRound().getNextRound().getGames().size(), is(0));

    }

    @Test
    public void assignPlayerToKoField8() {

        List<TournamentGroup> groups = createPlayerForKOField(8);

        KOField field = drawKoFieldManager.createKOField(RoundType.QUARTER);
        List<TournamentSingleGame> games = drawKoFieldManager.assignPlayerToKoField(field, groups);

        assertEquals(1, games.get(0).getPlayer1().ranking);
    }

    @Test
    public void assignPlayerToKoField64() {

        List<TournamentGroup> groups = createPlayerForKOField(52);

        KOField field = drawKoFieldManager.createKOField(RoundType.R64);
        List<TournamentSingleGame> games = drawKoFieldManager.assignPlayerToKoField(field, groups);

        assertEquals(1, games.get(0).getPlayer1().ranking);
        assertEquals(32, games.size());
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

    private List<TournamentGroup> createPlayerForKOField(int size) {
        List<TournamentGroup> groups = new ArrayList<>();
        int ttr = 1500;
        int rank = 1;
        List<String[]> names = names();
        for (int i = 0; i < size / 2; i++) {
            TournamentGroup group = new TournamentGroup();
            List<PlayerStatistic> ranking = new ArrayList<>();
            PlayerStatistic ps = new PlayerStatistic();
            if (i < names.size()) {
                ps.player = new TournamentPlayer(1L, names.get(i)[0], names.get(i)[1]);
            } else {
                ps.player = new TournamentPlayer(1L, i + "1", i + "1");
            }
            ps.player.setQttr(ttr--);
            ps.player.ranking = rank++;
            ranking.add(ps);
            ps = new PlayerStatistic();
            ps.player = new TournamentPlayer(1L, i + "2", i + "2");
            ps.player.setQttr(ttr--);
            ranking.add(ps);
            group.setRanking(ranking);
            groups.add(group);

        }
        return groups;
    }

    @Test
    public void calcKOSize() {
        TournamentClass tc = new TournamentClass();
        tc.createPhaseCombination(PhaseCombination.GK);
        tc.setActivePhase(0);
//        tc.setGroupCount(4); todo
        for (int i = 0; i < 4; i++) {
            TournamentGroup tg = new TournamentGroup();
            tc.addGroup(tg);
            for (int j = 0; j < 4; j++) {
                tg.addGame(new TournamentSingleGame());

            }
        }
        assertThat(drawKoFieldManager.calcKOSize(tc), is(RoundType.QUARTER));
    }
}