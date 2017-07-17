/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

import java.util.List;

/**
 * Created by J. Melzer on 17.07.2017.
 */
public class SwissDraw {
    List<TournamentSingleGameDTO> games;

    TournamentPlayerDTO freilos;

    public SwissDraw() {
    }

    public SwissDraw(List<TournamentSingleGameDTO> games) {
        this.games = games;
    }

    public List<TournamentSingleGameDTO> getGames() {
        return games;
    }

    public void setGames(List<TournamentSingleGameDTO> games) {
        this.games = games;
    }

    public TournamentPlayerDTO getFreilos() {
        return freilos;
    }

    public void setFreilos(TournamentPlayerDTO freilos) {
        this.freilos = freilos;
    }
}
