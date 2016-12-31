package com.jmelzer.jitty.model.dto;

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
    String winnerName;
    String tcName;
    String gameName;
    String roundOrGroupName;
    TournamentGroupDTO group;
    RoundDTO round;
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
    List<GameSetDTO> sets = new ArrayList<>();

    private Long id;
    private int winner = -1;
    private Long tid;

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

    public List<GameSetDTO> getSets() {
        return sets;
    }

    public void setSets(List<GameSetDTO> sets) {
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
        if (group != null) {
            roundOrGroupName = group.getName();
        }
    }

    public String getTcName() {
        return tcName;
    }

    public void setTcName(String tcName) {
        this.tcName = tcName;
    }

    @Override
    public String toString() {
        return "TournamentSingleGameDTO{" +
                "player1=" + player1 +
                ", player2=" + player2 +
                ", group=" + group +
                ", played=" + played +
                ", called=" + called +
                ", startTime=" + startTime +
                ", tableNo=" + tableNo +
                ", sets=" + sets +
                ", id=" + id +
                ", winner=" + winner +
                '}';
    }

    public void addSet(GameSetDTO gameSetDTO) {
        sets.add(gameSetDTO);
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public RoundDTO getRound() {
        return round;
    }

    public void setRound(RoundDTO round) {
        this.round = round;
        if (round != null) {
            roundOrGroupName = round.getRoundType().getName();
        }
    }

    public String getRoundOrGroupName() {
        return roundOrGroupName;
    }

    public void setRoundOrGroupName(String roundOrGroupName) {
        this.roundOrGroupName = roundOrGroupName;
    }
}
