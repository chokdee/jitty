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

    public TournamentGroup getGroup() {
        return group;
    }

    public void setGroup(TournamentGroup group) {
        this.group = group;
    }

    public SwissSystemPhase() {
    }

    public SwissSystemPhase(String name) {
        group = new TournamentGroup(name);
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
        for (TournamentSingleGame game : games) {
            if (!game.isFinished()) {
                return false;
            }
        }
        return true;
    }
}
