package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Turniergruppe
 */
@Entity
@Table(name = "tournament_group")
public class TournamentGroup {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "name", length = 10)
    private String name;

    /**
     * Assoc to the player in the group.
     */
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "TG_PLAYER")
    List<TournamentPlayer> players = new ArrayList<>();

    public TournamentGroup() {
    }

    public TournamentGroup(String name) {
        this.name = name;
    }

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

    public void setPlayers(List<TournamentPlayer> players) {
        this.players = players;
    }

    public List<TournamentPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void addPlayer(TournamentPlayer player) {
        players.add(player);
    }

    @Override
    public String toString() {
        String s = "\n----------------------\nKlasse " + name + "\n";
        for (TournamentPlayer player : players) {
            s += player.getFirstName() + " " + player.getLastName() + " " + player.getQttr() + "\n";
        }
        s += "--------------------";
        return s;
    }
}
