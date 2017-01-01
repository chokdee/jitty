/*
 * Copyright (c) 2016.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

import com.jmelzer.jitty.model.TournamentSystem;

/**
 * Created by J. Melzer on 28.12.2016.
 */
public abstract class PhaseDTO {

    private TournamentSystem system;

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
}
