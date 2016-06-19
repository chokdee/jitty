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
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    Round nextRound;
    @Column
    private int size;


    @OneToMany(cascade = CascadeType.ALL)
    List<TournamentSingleGame> games = new ArrayList<>();

    @Column
    private RoundType roundType;

    public Round() {
    }

    public Round(RoundType roundType) {
        this.roundType = roundType;
    }

    public Round(int size) {
        this.size = size;
    }

    public void addGame(TournamentSingleGame tournamentSingleGame) {
        games.add(tournamentSingleGame);
    }

    public void setNextRound(Round nextRound) {
        this.nextRound = nextRound;
    }

    public Long getId() {
        return id;
    }

    public Round getNextRound() {
        return nextRound;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public List<TournamentSingleGame> getGames() {
        return Collections.unmodifiableList(games);
    }

    public void addAllGames(List<TournamentSingleGame> games) {
        this.games.addAll(games);
    }
}
