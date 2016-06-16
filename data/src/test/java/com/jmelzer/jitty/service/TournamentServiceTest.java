package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        Queue<TournamentSingleGame> gameQueue = new LinkedList<>();

        TournamentGroup group = prepareGroupWithPlayerAndGames();
        service.addGroup(group);

        service.addPossibleGamesToQueue(gameQueue, service.getBusyGames());
        assertEquals(2, gameQueue.size());

        TournamentSingleGame game = gameQueue.poll();
        game.setCalled(true);
        assertEquals(1, gameQueue.size());
        service.addPossibleGamesToQueue(gameQueue, service.getBusyGames());
        assertEquals("player busy no new player can be added", 1, gameQueue.size());

        game = gameQueue.poll();
        game.setCalled(true);
        assertEquals(0, gameQueue.size());

        service.addPossibleGamesToQueue(gameQueue, service.getBusyGames());
        assertEquals(2, gameQueue.size());
    }

    @Test
    public void createKOField() {
        KOField field = service.createKOField(RoundType.R64);
        assertNotNull(field.getRound().getNextRound().getNextRound().getNextRound().getNextRound().getNextRound());
    }

    @Test
    public void assignPlayerToKoField32() {

        createPlayerForKOField();

        KOField field = service.createKOField(RoundType.R32);
        TournamentPlayer[] players = service.assignPlayerToKoField(field);

        assertEquals(1, players[1].ranking);
        assertEquals(2, players[32].ranking);
        assertEquals(3, players[17].ranking);
        assertEquals(4, players[16].ranking);
        assertEquals(5, players[9].ranking);
        assertEquals(6, players[24].ranking);
        assertEquals(7, players[25].ranking);
        assertEquals(8, players[8].ranking);
        assertEquals(9, players[5].ranking);
        assertEquals(10, players[28].ranking);
        assertEquals(11, players[21].ranking);
        assertEquals(12, players[12].ranking);
        assertEquals(13, players[13].ranking);
        assertEquals(14, players[19].ranking);
        assertEquals(15, players[30].ranking);
        assertEquals(16, players[4].ranking);
    }

    @Test
    public void assignPlayerToKoField16() {

        createPlayerForKOField();

        KOField field = service.createKOField(RoundType.R16);
        TournamentPlayer[] players = service.assignPlayerToKoField(field);
        assertEquals(1, players[1].ranking);
        assertEquals(2, players[16].ranking);
        assertEquals(3, players[9].ranking);
        assertEquals(4, players[8].ranking);
        assertEquals(5, players[5].ranking);
        assertEquals(6, players[12].ranking);
        assertEquals(7, players[13].ranking);
        assertEquals(8, players[4].ranking);
        assertEquals(9, players[3].ranking);
        assertEquals(10, players[14].ranking);
        assertEquals(11, players[11].ranking);
        assertEquals(12, players[6].ranking);
        assertEquals(13, players[7].ranking);
        assertEquals(14, players[10].ranking);
        assertEquals(15, players[15].ranking);
        assertEquals(16, players[2].ranking);


    }

    @Test
    public void assignPlayerToKoField8() {

        createPlayerForKOField();

        KOField field = service.createKOField(RoundType.QUARTER);
        TournamentPlayer[] players = service.assignPlayerToKoField(field);

        assertEquals(1, players[1].ranking);
    }

    private void createPlayerForKOField() {
        int ttr = 1500;
        int rank = 1;
        for (int i = 0; i < 8; i++) {
            TournamentGroup group = new TournamentGroup();
            List<TournamentService.PS> ranking = new ArrayList<>();
            TournamentService.PS ps = new TournamentService.PS();
            ps.player = new TournamentPlayer(1L, i + "1", i + "1");
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


    private void addGame(TournamentGroup group, int i, int j) {
        TournamentSingleGame game;
        game = new TournamentSingleGame();
        game.setPlayer1(group.getPlayers().get(i));
        game.setPlayer2(group.getPlayers().get(j));
        group.addGame(game);
    }
}