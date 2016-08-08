package com.jmelzer.jitty.model.dto;

import com.jmelzer.jitty.model.GameSet;
import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.TournamentPlayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by J. Melzer on 03.06.2016.
 * Represents one game.
 */
public class TournamentSingleGameDTO {
    TournamentPlayerDTO player1;
    TournamentPlayerDTO player2;
    TournamentGroupDTO group;
    /**
     * Bereits gespielt?
     */
    boolean played;
    /**
     * Bereits aufgerufen?
     */
    boolean called;
    Date startTime;
    /**
     * Nummer des zugewiesenen Tisches.
     */
    Integer tableNo;
    List<GameSet> sets = new ArrayList<>();

    private Long id;
    private int winner = -1;

    public TournamentPlayerDTO getPlayer1() {
        return player1;
    }

    public void setPlayer1(TournamentPlayerDTO player1) {
        this.player1 = player1;
    }

    public TournamentPlayerDTO getPlayer2() {
        return player2;
    }

    public void setPlayer2(TournamentPlayerDTO player2) {
        this.player2 = player2;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getTableNo() {
        return tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    public List<GameSet> getSets() {
        return sets;
    }

    public void setSets(List<GameSet> sets) {
        this.sets = sets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public TournamentGroupDTO getGroup() {
        return group;
    }

    public void setGroup(TournamentGroupDTO group) {
        this.group = group;
    }
}
