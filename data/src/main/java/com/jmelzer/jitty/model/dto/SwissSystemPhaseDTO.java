/*
 * Copyright (c) 2017.
 * J. Melzer
 */


package com.jmelzer.jitty.model.dto;

/**
 * Created by J. Melzer o
 */
public class SwissSystemPhaseDTO extends PhaseDTO {

    /**
     * Assoc to the groups in the class.
     */
    private TournamentGroupDTO group;

    private int round = 1;

    private int maxRounds = 6;

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public TournamentGroupDTO getGroup() {
        return group;
    }

    public void setGroup(TournamentGroupDTO group) {
        this.group = group;
    }
}
