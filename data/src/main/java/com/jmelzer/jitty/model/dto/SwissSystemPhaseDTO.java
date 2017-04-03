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

    public TournamentGroupDTO getGroup() {
        return group;
    }

    public void setGroup(TournamentGroupDTO group) {
        this.group = group;
    }
}
