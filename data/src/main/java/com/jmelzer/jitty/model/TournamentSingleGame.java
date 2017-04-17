/*
 * Copyright (c) 2017.
 * J. Melzer
 */

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
@Table(name = "TOURNAMENT_SINGLE_GAME")
public class TournamentSingleGame {
    //TC Name
    @Column(nullable = false, name = "tournament_class_name")
    String tcName;

    @Column(name = "NAME")
    String gameName;

    //todo add Schiedsrichter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "t_id")
    private Long tid;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "player1_id", foreignKey = @ForeignKey(name = "FK_P1"))
    private TournamentPlayer player1;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "player2_id", foreignKey = @ForeignKey(name = "FK_P2"))
    private TournamentPlayer player2;

    /**
     * Bereits gespielt?
     */
    @Column(nullable = false, name = "played")
    private boolean played;

    /**
     * Bereits aufgerufen?
     */
    @Column(nullable = false, name = "called")
    private boolean called;

    @Column(nullable = true, name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(nullable = true, name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    /**
     * Nummer des zugewiesenen Tisches.
     */
    @Column(nullable = true, name = "table_no")
    private Integer tableNo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TOURNAMENT_SINGLE_GAME_SET",
            joinColumns = @JoinColumn(name = "TOURNAMENT_SINGLE_GAME_ID"),
            inverseJoinColumns = @JoinColumn(name = "SETS_ID"))
    @OrderBy("id")
    private List<GameSet> sets = new ArrayList<>();

    @ManyToOne()
    private TournamentGroup group;

    @ManyToOne
    @JoinColumn(name = "ROUND_ID")
    private Round round;

    //-1 unset, 1=home , 2=guest
    private int winner = -1;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "NEXT_GAME_ID")
    private TournamentSingleGame nextGame;

    @ManyToOne(targetEntity = GameQueue.class)
    @JoinColumn(name = "GAME_QUEUE_ID")
    private GameQueue gameQueue;

    public TournamentSingleGame() {
    }

    public TournamentSingleGame(String tcName, Long tId) {
        this.tcName = tcName;
        this.tid = tId;
    }

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
        if (player1 != null && player1.equals(TournamentPlayer.BYE)) {
            return;
        }
        this.player1 = player1;
        if (player1 != null) {
            player1.addGame(this);
        }
    }

    public TournamentPlayer getPlayer2() {
        return player2;
    }

    public void setPlayer2(TournamentPlayer player2) {

        if (player2 != null && player2.equals(TournamentPlayer.BYE)) {
            return;
        }
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
        if (winner > 2) {
            throw new IllegalArgumentException("winner must be 1, 2 or -1");
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

    public String getResultInShort(TournamentPlayer own) {
        String s = "";
        if (winner == -1) {
            return "0:0";
        }
        int setsWon = 0;
        int setsLost = 0;
        for (GameSet gameSet : sets) {
            if (gameSet.getPoints1() < gameSet.getPoints2()) {
                setsLost++;
            } else {
                setsWon++;
            }
        }
        if (player1.equals(own)) {
            s += setsWon + ":" + setsLost;
        } else {
            s += setsLost + ":" + setsWon;

        }
        return s;
    }

    public boolean isFinishedOrCalled() {
        return isFinished() || called;
    }

    public boolean isFinished() {
        return winner > -1;
    }

    public TournamentPlayer getWinningPlayer() {
        if (winner == 1) {
            return player1;
        }

        return player2;
    }

    @Override
    public int hashCode() {
        int result = player1 != null ? player1.hashCode() : 0;
        result = 31 * result + (player2 != null ? player2.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TournamentSingleGame game = (TournamentSingleGame) o;

        if (player1 != null ? !player1.equals(game.player1) : game.player1 != null) {
            return false;
        }
        if (player2 != null ? !player2.equals(game.player2) : game.player2 != null) {
            return false;
        }
        return id != null ? id.equals(game.id) : game.id == null;

    }

    @Override
    public String toString() {
        return "TournamentSingleGame{" +
                "id='" + id + '\'' +
                ", p1=" + (player1 != null ? player1.getId() : "null") +
                ", p2=" + (player2 != null ? player2.getId() : "null") +
                ", tid=" + tid+
                ", tcName=" + tcName+
                '}';
    }

    public TournamentGroup getGroup() {
        return group;
    }

    public void setGroup(TournamentGroup group) {
        this.group = group;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public String getTcName() {
        return tcName;
    }

    public void setTcName(String tcName) {
        this.tcName = tcName;
    }

    public TournamentSingleGame getNextGame() {
        return nextGame;
    }

    public void setNextGame(TournamentSingleGame nextGame) {
        this.nextGame = nextGame;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean isEmpty() {
        return player1 == null || player2 == null;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public GameQueue getGameQueue() {
        return gameQueue;
    }

    public void setGameQueue(GameQueue gameQueue) {
        this.gameQueue = gameQueue;
    }
}
