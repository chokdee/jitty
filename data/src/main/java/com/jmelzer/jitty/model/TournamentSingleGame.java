package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by J. Melzer on 03.06.2016.
 * Represents one game.
 */
@Entity
@Table(name = "tournament_single_game")
public class TournamentSingleGame {
    @OneToOne(cascade = CascadeType.DETACH)
    TournamentPlayer player1;
    @OneToOne(cascade = CascadeType.DETACH)
    TournamentPlayer player2;
    /**
     * Bereits gespielt?
     */
    @Column(nullable = false, name = "played")
    boolean played;
    /**
     * Bereits aufgerufen?
     */
    @Column(nullable = false, name = "called")
    boolean called;
    @Column(nullable = true, name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    Date startTime;
    /**
     * Nummer des zugewiesenen Tisches.
     */
    @Column(nullable = true, name = "table_no")
    Integer tableNo;
    @OneToMany(cascade = CascadeType.ALL)
    List<GameSet> sets = new ArrayList<>();

    //todo add Schiedsrichter
    @Id
    @GeneratedValue
    private Long id;
    private int winner = -1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TournamentPlayer getPlayer1() {
        return player1;
    }

    public void setPlayer1(TournamentPlayer player1) {
        this.player1 = player1;
        if (player1 != null) {
            player1.addGame(this);
        }
    }

    public TournamentPlayer getPlayer2() {
        return player2;
    }

    public void setPlayer2(TournamentPlayer player2) {
        this.player2 = player2;
        if (player2 != null) {
            player2.addGame(this);
        }
    }

    public List<GameSet> getSets() {
        return sets;
    }

    public void setSets(List<GameSet> sets) {
        this.sets = sets;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        if (winner < 1 || winner > 2) {
            throw new IllegalArgumentException("winner must be 1 or 2");
        }
        this.winner = winner;
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

    @Override
    public String toString() {
        return "  " +
                (player1 != null ? player1.getFullName() : "undefined") +
                " ---> " + (player2 != null ? player2.getFullName() : "undefined");
    }

    public void addSet(GameSet gameSet) {
        sets.add(gameSet);
    }

    public String printResult() {
        String s = "";
        if (winner == -1) {
            return " not played";
        }
        s += "Player " + winner + " won with set statistics (" + printSets() + ")";
        return s;
    }

    private String printSets() {
        String s = "";
        for (GameSet set : sets) {
            s += set.points1 + ":" + set.points2 + ", ";
        }
        return s.trim().substring(0, s.length() - 2);
    }

    public boolean isFinishedOrCalled() {
        return winner > -1 || called;
    }

    public TournamentPlayer getWinningPlayer() {
        if (winner == 1) {
            return player1;
        }

        return player2;
    }
}
