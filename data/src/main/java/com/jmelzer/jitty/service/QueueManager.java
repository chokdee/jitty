/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.GameQueueRepository;
import com.jmelzer.jitty.model.GameQueue;
import com.jmelzer.jitty.model.TournamentSingleGame;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.jmelzer.jitty.service.CopyManager.copy;

/**
 * Created by J. Melzer on 22.01.2017.
 */
@Component
public class QueueManager {
    //todo make persistent
    List<TournamentSingleGame> busyGames = new ArrayList<>();

    @Resource
    GameQueueRepository gameQueueRepository;

    public int getQueueSize() {
        return getQueue().size();
    }

    List<TournamentSingleGame> getQueue() {
        return getQueueO().getGames();
    }

    GameQueue getQueueO() {
        return gameQueueRepository.getOne(1L);
    }

    public List<TournamentSingleGame> getBusyGamesOriginal() {
        return busyGames;
    }

    public List<TournamentSingleGameDTO> getBusyGames(Long actualTournamentId) {
        List<TournamentSingleGameDTO> list = new ArrayList<>();
        for (TournamentSingleGame game : busyGames) {
            if (game.getTid().equals(actualTournamentId)) {
                list.add(copy(game, false));
            }
        }
        return list;
    }

    public void removeBusyGame(TournamentSingleGame game) {
        busyGames.remove(game);
    }

    @Transactional
    public List<TournamentSingleGameDTO> listQueue(Long tid) {
        List<TournamentSingleGameDTO> list = new ArrayList<>();
        for (TournamentSingleGame game : getQueue()) {
            if (game.getTid().equals(tid)) {
                list.add(copy(game, false));
            }
        }
        return list;
    }


    @Transactional
    public void moveGameBackToPossibleGames(TournamentSingleGame game) {
        GameQueue q = getQueueO();
        q.addGame(game);
        busyGames.remove(game);
        gameQueueRepository.save(q);
    }

    @Transactional
    public void moveGameToBusyGames(TournamentSingleGame game) {
        if (game.getTableNo() == null) {
            throw new IllegalArgumentException("game must have a table");
        }

        busyGames.add(game);
        GameQueue q = getQueueO();
        q.removeGame(game);
        gameQueueRepository.saveAndFlush(q);

    }

    public void removeBuyGame(TournamentSingleGame game) {
        busyGames.remove(game);
    }

    List<TournamentSingleGame> getGamesFromQueue(int count, Long tid) {
        List<TournamentSingleGame> q = getQueue();
        int max = q.size();
        int i = 0;
        List<TournamentSingleGame> result = new ArrayList<>();
        while (result.size() < count && i < max) {
            TournamentSingleGame game = q.get(i);
            if (game.getTid().equals(tid)) {
                result.add(game);
            }
            i++;
        }
        return result;
    }

    public void removeAllFromClass(String name) {
        busyGames.removeIf(g -> g.getTcName().equals(name));
    }

    @Transactional
    public void clearAll() {
        GameQueue queue = getQueueO();
        queue.clear();
        gameQueueRepository.saveAndFlush(queue);
        busyGames.clear();

    }

    @Transactional
    public void addGame(TournamentSingleGame game) {
        GameQueue queue = getQueueO();
        queue.addGame(game);
        gameQueueRepository.save(queue);
    }

    public void addAll(List<TournamentSingleGame> toAddGames) {
        GameQueue queue = getQueueO();
        for (TournamentSingleGame game : toAddGames) {
            queue.addGame(game);
        }
        gameQueueRepository.save(queue);
    }
}
