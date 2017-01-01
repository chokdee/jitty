package com.jmelzer.jitty.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 01.08.2016.
 * Turniergruppe
 */
public class TournamentGroupDTO {

    private Long id;

    private String name;
    /**
     * Assoc to the player in the group.
     */
    List<TournamentPlayerDTO> players = new ArrayList<>();

    TournamentClassDTO tournamentClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TournamentPlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<TournamentPlayerDTO> players) {
        this.players = players;
    }

    public void addPlayer(TournamentPlayerDTO playerDTO) {
        players.add(playerDTO);
    }

    public TournamentClassDTO getTournamentClass() {
        return tournamentClass;
    }

    public void setTournamentClass(TournamentClassDTO tournamentClass) {
        this.tournamentClass = tournamentClass;
    }
}
