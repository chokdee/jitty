package com.jmelzer.jitty.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 16.10.2016.
 */
public class RoundDTO {
    RoundDTO nextRound;
    List<TournamentSingleGameDTO> games = new ArrayList<>();
    private Long id;
    private int gameSize;
    private RoundTypeDTO roundType;

    public RoundDTO() {
    }

    public RoundDTO(RoundTypeDTO roundType) {
        this.roundType = roundType;
    }

    public RoundDTO(int gameSize) {
        this.gameSize = gameSize;
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

    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
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
