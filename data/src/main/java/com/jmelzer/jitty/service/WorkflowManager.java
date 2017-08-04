/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.SwissSystemPhase;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.dto.TournamentClassStatus;
import org.springframework.stereotype.Component;

import static com.jmelzer.jitty.model.dto.TournamentClassStatus.*;

/**
 * Created by J. Melzer on 03.08.2017.
 */
@Component
public class WorkflowManager {

    public TournamentClassStatus calcStatus(TournamentClass tc) {


        if (tc.finished()) {
            return FINISHED;
        }

        if (!tc.getRunning()) {
            return TournamentClassStatus.NOTSTARTED;
        } else {
            if (tc.getSystem() == null) {
                return TournamentClassStatus.NOT_INITIALIZED;
            }
            if (tc.getActivePhase() instanceof SwissSystemPhase) {
                return calcStatusForSwiss(tc);
            }
            TournamentClassStatus x = calcStatusForKOGroup(tc);
            if (x != null) {
                return x;
            }
        }
        throw new RuntimeException("could not calculate status");
    }

    private TournamentClassStatus calcStatusForSwiss(TournamentClass tc) {
        SwissSystemPhase phase = (SwissSystemPhase) tc.getActivePhase();
        if (tc.getPhaseCount() > tc.getActivePhaseNo() + 1) {
            phase = (SwissSystemPhase) tc.getAllPhases().get(tc.getPhaseCount() - 1); //latest
        }

        if (phase.isFinished()) {
            return SWISS_PHASE_FINISHED;
        } else if (phase.areGamesPlayed()) {
            return SWISS_PHASE_RUNNING;
        } else if (phase.hasGames()) {
            return SWISS_PHASE_DRAW_BUT_NOT_STARTED;
        } else {
            return TournamentClassStatus.NOTSTARTED;
        }
    }

    private TournamentClassStatus calcStatusForKOGroup(TournamentClass tc) {
        boolean hasResults = tc.getActivePhase().areGamesPlayed();
        if (tc.getActivePhaseNo() == 0) {
            if (hasResults) {
                if (tc.getActivePhase().isFinished()) {
                    return PHASE1_FINISHED;
                }
                return PHASE1_AND_RESULTS;
            } else {
                return PHASE1_STARTED_NOT_CALLED;
            }
        } else if (tc.getActivePhaseNo() == 1) {
            if (hasResults) {
                return PHASE2_AND_RESULTS;
            } else {
                return PHASE2_STARTED_NOT_CALLED;
            }
        }
        return null;
    }
}
