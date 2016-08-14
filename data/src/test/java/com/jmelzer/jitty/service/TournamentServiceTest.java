package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentSingleGameRepository;
import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.GameSetDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 11.06.2016.
 */
public class TournamentServiceTest {
    TournamentService service = new TournamentService();

    @Before
    public void setup() {
        service = new TournamentService();
    }

    @Test
    public void testAddPossibleGamesToQueue() throws Exception {

        TournamentGroup group = prepareGroupWithPlayerAndGames();
        service.addGroup(group);

        service.addPossibleGroupGamesToQueue(service.getGroups());
        assertEquals(2, service.getQueueSize());

        TournamentSingleGame game = service.poll();
        game.setCalled(true);
        assertEquals(1, service.getQueueSize());
        service.addPossibleGroupGamesToQueue(service.getGroups());
        assertEquals("player busy no new player can be added", 1, service.getQueueSize());

        game = service.poll();
        game.setCalled(true);
        assertEquals(0, service.getQueueSize());

        service.addPossibleGroupGamesToQueue(service.getGroups());
        assertEquals(2, service.getQueueSize());
    }

    @Test
    public void createKOField() {
        KOField field = service.createKOField(RoundType.R64);
        assertNotNull(field.getRound().getNextRound().getNextRound().getNextRound().getNextRound().getNextRound());
    }

    @Test
    public void assignPlayerToKoField32() {

        createPlayerForKOField(30);

        KOField field = service.createKOField(RoundType.R32);
        List<TournamentSingleGame> games = service.assignPlayerToKoField(field);

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

        createPlayerForKOField(16);

        KOField field = service.createKOField(RoundType.R16);
        List<TournamentSingleGame> games = service.assignPlayerToKoField(field);
        assertEquals("Ma", games.get(0).getPlayer1().getFirstName());
        assertEquals("Fan", games.get(7).getPlayer2().getFirstName());
        //todo etc

        for (TournamentSingleGame game : games) {
            assertNotNull("all player must be filled", game.getPlayer1());
            assertNotNull("all player must be filled", game.getPlayer2());
        }

    }

    @Test
    public void assignPlayerToKoField8() {

        createPlayerForKOField(8);

        KOField field = service.createKOField(RoundType.QUARTER);
        List<TournamentSingleGame> games = service.assignPlayerToKoField(field);

        assertEquals(1, games.get(0).getPlayer1().ranking);
    }

    @Test
    public void assignPlayerToKoField64() {

        createPlayerForKOField(52);

        KOField field = service.createKOField(RoundType.R64);
        List<TournamentSingleGame> games = service.assignPlayerToKoField(field);

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

    private void createPlayerForKOField(int size) {
        int ttr = 1500;
        int rank = 1;
        List<String[]> names = names();
        for (int i = 0; i < size / 2; i++) {
            TournamentGroup group = new TournamentGroup();
            List<TournamentService.PS> ranking = new ArrayList<>();
            TournamentService.PS ps = new TournamentService.PS();
            if (i < names.size()) {
                ps.player = new TournamentPlayer(1L, names.get(i)[0], names.get(i)[1]);
            } else {
                ps.player = new TournamentPlayer(1L, i + "1", i + "1");
            }
            ps.player.setQttr(ttr--);
            ps.player.ranking = rank++;
            ranking.add(ps);
            ps = new TournamentService.PS();
            ps.player = new TournamentPlayer(1L, i + "2", i + "2");
            ps.player.setQttr(ttr--);
            ranking.add(ps);
            group.setRanking(ranking);
            service.addGroup(group);

        }
    }

    private TournamentGroup prepareGroupWithPlayerAndGames() {
        TournamentGroup group = new TournamentGroup("A");
        group.addPlayer(new TournamentPlayer(1L, "1", "1"));
        group.addPlayer(new TournamentPlayer(2L, "2", "2"));
        group.addPlayer(new TournamentPlayer(3L, "3", "3"));
        group.addPlayer(new TournamentPlayer(4L, "4", "4"));


        addGame(group, 0, 1);
        addGame(group, 0, 2);
        addGame(group, 0, 3);
        addGame(group, 1, 2);
        addGame(group, 1, 3);
        addGame(group, 2, 3);
        return group;
    }

    @Test
    public void calcRankingForGroup() {
        TournamentGroup group = prepareGroupWithPlayerAndGames();
        //first player 3:0
        //second ... last 1:2
        for (int i = 0; i < 6; i++) {
            TournamentSingleGame game = group.getGames().get(i);
            if (i < 3) {
                game.setWinner(1);
            }
        }
        service.calcRankingForGroup(group);
    }

    @Test
    public void testcalcGroupGames() {
        TournamentSingleGameRepository repos = Mockito.mock(TournamentSingleGameRepository.class);
        service.tournamentSingleGameRepository = repos;
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

        service.calcGroupGames(Arrays.asList(group, group2, group3, group4));
        assertThat(group.getGames().size(), is(6));
        assertThat(group2.getGames().size(), is(6));
        assertThat(group3.getGames().size(), is(3));
        assertThat(group4.getGames().size(), is(3));
    }

    private void addGame(TournamentGroup group, int i, int j) {
        TournamentSingleGame game;
        game = new TournamentSingleGame();
        game.setPlayer1(group.getPlayers().get(i));
        game.setPlayer2(group.getPlayers().get(j));
        group.addGame(game);
    }

    @Test
    public void calcWinner() {
        TournamentSingleGameDTO game = new TournamentSingleGameDTO();
        game.addSet(new GameSetDTO(11, 9));
        assertThat(service.calcWinner(game).getWinner(), is(1));
        game.addSet(new GameSetDTO(11, 13));
        try {
            service.calcWinner(game);
            fail("no winner");
        } catch (IllegalArgumentException e) {
            //ok
        }
        game.addSet(new GameSetDTO(11, 13));
        assertThat(service.calcWinner(game).getWinner(), is(2));
    }
}