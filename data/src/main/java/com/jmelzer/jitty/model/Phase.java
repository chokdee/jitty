/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;

/**
 * Created by J. Melzer on 28.12.2016.
 */
@Entity
@Inheritance( strategy = InheritanceType.JOINED )
public abstract class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = TournamentSystem.class)
    @JoinColumn(name = "S_ID")
    private TournamentSystem system;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TournamentSystem getSystem() {
        return system;
    }

    public void setSystem(TournamentSystem system) {
        this.system = system;
    }

    public abstract boolean areGamesPlayed();
    /** if user decided to reset a phase*/
    public abstract void resetGames();

    public abstract boolean isFinished();
}
