package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.TournamentSingleGame;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

/**
 * Created by J. Melzer on 11.06.2016.
 */
public class TournamentServiceTest {
    TournamentService service = new TournamentService();

    @Before
    public void setuo() {
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