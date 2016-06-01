package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Turnier-Klasse
 */
@Entity
@Table(name = "tournament_class")
public class TournamentClass {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    /** Assoc to the player in the class. */
    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "TP_ID")
    List<TournamentPlayer> players = new ArrayList<>();

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

    public List<TournamentPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void addPlayer(TournamentPlayer player) {
        players.add(player);
    }
}
