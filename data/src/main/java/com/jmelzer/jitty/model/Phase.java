package com.jmelzer.jitty.model;

import javax.persistence.*;

/**
 * Created by J. Melzer on 28.12.2016.
 */
@Entity
@Inheritance( strategy = InheritanceType.JOINED )
public abstract class Phase {

    @ManyToOne()
    private TournamentSystem system;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public abstract boolean isFinished();
}
