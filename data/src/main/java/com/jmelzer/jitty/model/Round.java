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
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "prevRound")
    @JoinColumn(name = "NEXT_ROUND_ID")
    Round nextRound;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "PREV_ROUND_ID")
    Round prevRound;
    @OneToOne(cascade = CascadeType.DETACH, mappedBy = "round")
    KOField koField;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "round")
    List<TournamentSingleGame> games = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "GAME_SIZE")
    private int gameSize;
    @Column(name = "ROUND_TYPE")
    private RoundType roundType;

    public Round() {
    }

    public Round(RoundType roundType) {
        this.roundType = roundType;
    }

    public Round(int gameSize) {
        this.gameSize = gameSize;
        roundType = RoundType.fromValue(gameSize * 2);
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
        nextRound.prevRound = this;
    }

    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public int playerSize() {
        return gameSize * 2;
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

    public Round getPrevRound() {
        return prevRound;
    }

    public void setPrevRound(Round prevRound) {
        this.prevRound = prevRound;
    }

    public KOField getKoField() {
        return koField;
    }

    public void setKoField(KOField koField) {
        this.koField = koField;
    }

    public KOField findKOField() {
        if (koField != null) {
            return koField;
        }
        if (prevRound != null) {
            return prevRound.findKOField();
        }
        throw new IllegalArgumentException("wether kofield or preRound must be set");
    }
}
