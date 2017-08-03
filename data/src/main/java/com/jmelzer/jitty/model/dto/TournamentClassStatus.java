/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

/**
 * Created by J. Melzer on 27.12.2016.
 */
public enum TournamentClassStatus {
    NOTSTARTED,

    PHASE1_STARTED_NOT_CALLED,
    PHASE1_AND_RESULTS,
    PHASE1_FINISHED,


    PHASE2_STARTED_NOT_CALLED,
    PHASE2_AND_RESULTS,
    PHASE2_FINISHED,

    SWISS_PHASE_DRAW_NOT_STARTED,
    SWISS_PHASE_RUNNING,
    SWISS_PHASE_FINISHED,

    NOT_INITIALIZED,
    FINISHED;


}
