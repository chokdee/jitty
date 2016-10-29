package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J. Melzer on 15.06.2016.
 */
@Entity
@Table(name = "round")
public class Round {
    @OneToOne(cascade = CascadeType.ALL)
    Round nextRound;
    @OneToMany(cascade = CascadeType.ALL)
    List<TournamentSingleGame> games = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private int gameSize;
    @Column
    private RoundType roundType;

    public Round() {
    }

    public Round(RoundType roundType) {
        this.roundType = roundType;
    }

    public Round(int gameSize) {
        this.gameSize = gameSize;
        roundType = RoundType.fromValue(gameSize *2);
    }

    public void addGame(TournamentSingleGame tournamentSingleGame) {
        games.add(tournamentSingleGame);
    }

    public Long getId() {
        return id;
    }

    public Round getNextRound() {
        return nextRound;
    }

    public void setNextRound(Round nextRound) {
        this.nextRound = nextRound;
    }

    public int getGameSize() {
        return gameSize;
    }
    public int playerSize() {
        return gameSize * 2;
    }
    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public List<TournamentSingleGame> getGames() {
        return Collections.unmodifiableList(games);
    }

    public void addAllGames(List<TournamentSingleGame> games) {
        this.games.addAll(games);
        for (TournamentSingleGame game : games) {
            game.setRound(this);
        }
    }

    public RoundType getRoundType() {
        return roundType;
    }

    public void setRoundType(RoundType roundType) {
        this.roundType = roundType;
    }
}
