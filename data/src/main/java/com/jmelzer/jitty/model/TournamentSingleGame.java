package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 03.06.2016.
 * Represents one game.
 */
@Entity
@Table(name = "tournament_single_game")
public class TournamentSingleGame {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH)
    TournamentPlayer player1;

    @OneToOne(cascade = CascadeType.DETACH)
    TournamentPlayer player2;

    @OneToMany(cascade = CascadeType.ALL)
    List<GameSet> sets = new ArrayList<>();

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
    }

    public TournamentPlayer getPlayer2() {
        return player2;
    }

    public void setPlayer2(TournamentPlayer player2) {
        this.player2 = player2;
    }

    public List<GameSet> getSets() {
        return sets;
    }

    public void setSets(List<GameSet> sets) {
        this.sets = sets;
    }
}
