/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J. Melzer on 28.12.2016.
 * Tournment system
 */
@Entity
@Table(name = "t_system")
public class TournamentSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "system", fetch = FetchType.LAZY)
    @OrderColumn(name = "INDEXP")
    private List<Phase> phases = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "TC_ID")
    private TournamentClass tournamentClass;

    public KOField findKOField() {
        for (Phase phase : phases) {
            if (phase instanceof KOPhase) {
                return ((KOPhase) phase).getKoField();
            }
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Phase getPhase(int idx) {
        if (idx < 0 || idx >= phases.size()) {
            return null;
        }
        return phases.get(idx);
    }

    public List<Phase> getPhases() {
        return Collections.unmodifiableList(phases);
    }

    public void addPhase(Phase phase) {
        phases.add(phase);
        phase.setSystem(this);
    }

    public void setKOField(KOField koField) {
        for (Phase phase : phases) {
            if (phase instanceof KOPhase) {
                ((KOPhase) phase).setKoField(koField);
            }
        }
    }

    public TournamentClass getTournamentClass() {
        return tournamentClass;
    }

    public void setTournamentClass(TournamentClass tournamentClass) {
        this.tournamentClass = tournamentClass;
    }


    public void clearPhases() {
        for (Phase phase : phases) {
            phase.setSystem(null);
        }
        phases.clear();
    }

    public int getPhaseCount() {
        return phases.size();
    }

    public Phase getLastPhase() {
        return phases.get(phases.size() - 1);
    }
}
