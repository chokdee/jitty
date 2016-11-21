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
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE} )
    @JoinColumn(name = "player1_id", foreignKey = @ForeignKey(name = "FK_P1"))
    TournamentPlayer player1;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "player2_id", foreignKey = @ForeignKey(name = "FK_P2"))
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
    @Column(nullable = true, name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    Date endTime;
    /**
     * Nummer des zugewiesenen Tisches.
     */
    @Column(nullable = true, name = "table_no")
    Integer tableNo;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TOURNAMENT_SINGLE_GAME_SET")
    @OrderBy("id")
    List<GameSet> sets = new ArrayList<>();

    @ManyToOne()
    TournamentGroup group;

    @ManyToOne
    Round round;

    //todo add Schiedsrichter
    @Id
    @GeneratedValue
    private Long id;

    private int winner = -1;

    //TC Name
    @Column(nullable = false, name = "tournament_class_name")
    String tcName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "NEXT_GAME_ID")
    private TournamentSingleGame nextGame;

    @Column(name = "NAME")
    String gameName;

    public TournamentSingleGame() {
    }

    public TournamentSingleGame(String tcName) {
        this.tcName = tcName;
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
        if (winner < 1 || winner > 2) {
            throw new IllegalArgumentException("winner must be 1 or -1");
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

    private String printSets() {
        String s = "";
        for (GameSet set : sets) {
            s += set.points1 + ":" + set.points2 + ", ";
        }
        return s.trim().substring(0, s.length() - 2);
    }

    public boolean isFinished() {
        return winner > -1;
    }
    public boolean isFinishedOrCalled() {
        return isFinished() || called;
    }

    public TournamentPlayer getWinningPlayer() {
        if (winner == 1) {
            return player1;
        }

        return player2;
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
    public int hashCode() {
        int result = player1 != null ? player1.hashCode() : 0;
        result = 31 * result + (player2 != null ? player2.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
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

    @Override
    public String toString() {
        return "TournamentSingleGame{" +
                "id='" + id + '\'' +
                ", p1=" + (player1 != null ? player1.getId() : "null") +
                ", p2=" + (player2 != null ? player2.getId() : "null") +
                '}';
    }

    public void setNextGame(TournamentSingleGame nextGame) {
        this.nextGame = nextGame;
    }

    public TournamentSingleGame getNextGame() {
        return nextGame;
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
}
