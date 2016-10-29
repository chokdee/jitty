package com.jmelzer.jitty.model.dto;

import com.jmelzer.jitty.model.RoundType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 16.10.2016.
 */
public class RoundDTO {
    RoundDTO nextRound;
    List<TournamentSingleGameDTO> games = new ArrayList<>();
    private Long id;
    private int size;
    private RoundTypeDTO roundType;

    public RoundDTO() {
    }

    public RoundDTO(RoundTypeDTO roundType) {
        this.roundType = roundType;
    }

    public RoundDTO(int size) {
        this.size = size;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoundDTO getNextRound() {
        return nextRound;
    }

    public void setNextRound(RoundDTO nextRound) {
        this.nextRound = nextRound;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public RoundTypeDTO getRoundType() {
        return roundType;
    }

    public void setRoundType(RoundTypeDTO roundType) {
        this.roundType = roundType;
    }

    public List<TournamentSingleGameDTO> getGames() {
        return games;
    }

    public void setGames(List<TournamentSingleGameDTO> games) {
        this.games = games;
    }

    public void addGame(TournamentSingleGameDTO gameDTO) {
        games.add(gameDTO);
    }
}
