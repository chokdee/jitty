package com.jmelzer.jitty.model;

import com.jmelzer.jitty.service.TournamentService;

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
    /**
     * Assoc to the player in the group.
     */
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "TG_PLAYER")
    List<TournamentPlayer> players = new ArrayList<>();
    /**
     * Assoc to the groups in the class.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
    List<TournamentSingleGame> games = new ArrayList<>();

    @Column(nullable = false, name = "name", length = 10)
    private String name;

    @ManyToOne
    @JoinColumn(name="TC_ID")
    TournamentClass tournamentClass;

    //todo we have to store it somewhere after group phase
    transient private List<TournamentService.PS> ranking;


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

    public List<TournamentPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void setPlayers(List<TournamentPlayer> players) {
        this.players = players;
    }

    public void addPlayer(TournamentPlayer player) {
        players.add(player);
    }

    public List<TournamentSingleGame> getGames() {
        return Collections.unmodifiableList(games);
    }

    public void addGames(List<TournamentSingleGame> games) {
        for (TournamentSingleGame game : games) {
            addGame(game);
        }
    }

    public void addGame(TournamentSingleGame game) {
        this.games.add(game);
        game.setGroup(this);
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

    public List<TournamentService.PS> getRanking() {
        return ranking;
    }

    public void setRanking(List<TournamentService.PS> ranking) {
        this.ranking = ranking;
    }

    public void setGames(List<TournamentSingleGame> games) {
        this.games = games;
    }

    public void removeByePlayer() {
        players.remove(TournamentPlayer.BYE);
    }

    public TournamentClass getTournamentClass() {
        return tournamentClass;
    }

    public void setTournamentClass(TournamentClass tournamentClass) {
        this.tournamentClass = tournamentClass;
    }
}
