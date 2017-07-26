/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by J. Melzer on 28.12.2016.
 */
@Entity
@Table(name = "swiss_system_phase")
public class SwissSystemPhase extends Phase {

    /**
     * Assoc to the groups in the class.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TournamentGroup group;

    @Column(name = "ROUND", nullable = false)
    private int round = 1;

    @Column(name = "MAX_ROUNDS", nullable = false)
    private int maxRounds = 6;

    public SwissSystemPhase() {
    }

    public SwissSystemPhase(String name) {
        group = new TournamentGroup(name);
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public TournamentGroup getGroup() {
        return group;
    }

    public void setGroup(TournamentGroup group) {
        this.group = group;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    @Override
    public boolean areGamesPlayed() {
        List<TournamentSingleGame> games = group.getGames();
        for (TournamentSingleGame game : games) {
            if (game.isPlayed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void resetGames() {
        group.removeAllGames();
    }

    @Override
    public boolean isFinished() {
        List<TournamentSingleGame> games = group.getGames();
        if (games.size() == 0) {
            return false;
        }

        for (TournamentSingleGame game : games) {
            if (!game.isFinished()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<TournamentSingleGame> getAllSingleGames() {
        return group.getGames();
    }

    public boolean hasRunningGames() {
        List<TournamentSingleGame> games = group.getGames();
        for (TournamentSingleGame game : games) {
            if (game.isCalled()) {
                return true;
            }
        }
        return false;
    }

    public boolean isLastPhase() {
        return maxRounds == round;
    }
}
