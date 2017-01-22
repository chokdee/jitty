/*
 * Copyright (c) 2016.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 30.12.2016.
 */
public class TournamentSystemDTO {

    private Long id;

    private List<PhaseDTO> phases = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addPhase(PhaseDTO phaseDTO) {
        phases.add(phaseDTO);
    }

    public List<PhaseDTO> getPhases() {
        return phases;
    }

    public void setPhases(List<PhaseDTO> phases) {
        this.phases = phases;
    }
}
