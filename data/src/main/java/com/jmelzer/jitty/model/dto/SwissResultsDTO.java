/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 20.07.2017.
 */
public class SwissResultsDTO {

    List<TournamentPlayerDTO> ranking;

    List<SwissRoundDTO> rounds = new ArrayList<>();

    public List<TournamentPlayerDTO> getRanking() {
        return ranking;
    }

    public void setRanking(List<TournamentPlayerDTO> ranking) {
        this.ranking = ranking;
    }

    public List<SwissRoundDTO> getRounds() {
        return rounds;
    }

    public void addRound(SwissRoundDTO round) {
        rounds.add(round);
    }

}
