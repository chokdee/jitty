/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.GameQueueRepository;
import com.jmelzer.jitty.model.GameQueue;
import com.jmelzer.jitty.model.TournamentSingleGame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by J. Melzer on 30.03.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class QueueManagerTest {

    @InjectMocks
    QueueManager queueManager = new QueueManager();

    @Mock
    GameQueueRepository gameQueueRepository;

    @Mock
    GameQueue gameQueue;

    @Before
    public void before() {
        when(gameQueueRepository.getOne(any())).thenReturn(gameQueue);
    }

    @Test
    public void removeBusyGame() {
        queueManager.removeBusyGame(new TournamentSingleGame());
    }

    @Test
    public void moveGameBackToPossibleGames() {
        when(gameQueueRepository.getOne(any())).thenReturn(new GameQueue());
        queueManager.moveGameBackToPossibleGames(new TournamentSingleGame());
    }

    @Test
    public void removeBusyGame1() {
    }

    @Test
    public void moveGameToBusyGames() {
        TournamentSingleGame game = new TournamentSingleGame();
        try {
            queueManager.moveGameToBusyGames(game);
            fail();
        } catch (IllegalArgumentException e) {
        }
        game.setTableNo(1);
        queueManager.moveGameToBusyGames(game);
        verify(gameQueueRepository).saveAndFlush(gameQueue);
    }

    @Test
    public void removeBuyGame() {
    }

    @Test
    public void getGamesFromQueue() {
        when(gameQueue.getGames()).thenReturn(Arrays.asList(new TournamentSingleGame("", 1L), new TournamentSingleGame("", 1L)));
        assertEquals(2, queueManager.getGamesFromQueue(2, 1L).size());
    }

    @Test
    public void removeAllFromClass() {
    }

    @Test
    public void clearAll() {
        queueManager.clearAll();

        verify(gameQueue).clear();
    }

    @Test
    public void addGame() {
        queueManager.addGame(new TournamentSingleGame());
        verify(gameQueueRepository).save(gameQueue);
    }

    @Test
    public void addAll() {
        queueManager.addAll(Collections.singletonList(new TournamentSingleGame()));
        verify(gameQueueRepository).save(gameQueue);
    }
}