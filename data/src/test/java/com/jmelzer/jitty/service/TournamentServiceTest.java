package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.TournamentSingleGame;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

/**
 * Created by J. Melzer on 11.06.2016.
 */
public class TournamentServiceTest {
    TournamentService service = new TournamentService();

    @Test
    public void testAddPossibleGamesToQueue() throws Exception {
        List<TournamentGroup> groups = new ArrayList<>();
        Queue<TournamentSingleGame> gameQueue = new LinkedList<>();

        TournamentGroup group = new TournamentGroup("A");
        group.addPlayer(new TournamentPlayer("1", "1"));
        group.addPlayer(new TournamentPlayer("2", "2"));
        group.addPlayer(new TournamentPlayer("3", "3"));
        group.addPlayer(new TournamentPlayer("4", "4"));
        groups.add(group);

        addGame(group, 0, 1);
        addGame(group, 0, 2);
        addGame(group, 0, 3);
        addGame(group, 1, 2);
        addGame(group, 1, 3);
        addGame(group, 2, 3);

        service.addPossibleGamesToQueue(groups, gameQueue, service.getBusyGames());
        assertEquals(2, gameQueue.size());

        TournamentSingleGame game = gameQueue.poll();
        game.setCalled(true);
        assertEquals(1, gameQueue.size());
        service.addPossibleGamesToQueue(groups, gameQueue, service.getBusyGames());
        assertEquals("player busy no new player can be added", 1, gameQueue.size());

        game = gameQueue.poll();
        game.setCalled(true);
        assertEquals(0, gameQueue.size());

        service.addPossibleGamesToQueue(groups, gameQueue, service.getBusyGames());
        assertEquals(2, gameQueue.size());
    }

    private void addGame(TournamentGroup group, int i, int j) {
        TournamentSingleGame game;
        game = new TournamentSingleGame();
        game.setPlayer1(group.getPlayers().get(i));
        game.setPlayer2(group.getPlayers().get(j));
        group.addGame(game);
    }
}