/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J. Melzer on 21.01.2017.
 */
@Entity
@Table(name = "GAME_QUEUE")
public class GameQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "gameQueue")
    private List<TournamentSingleGame> games = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TournamentSingleGame> getGames() {
        return Collections.unmodifiableList(games);
    }

    public void setGames(List<TournamentSingleGame> games) {
        this.games = games;
    }

    public void addGame(TournamentSingleGame game) {
        if (!games.contains(game)) {
            games.add(game);
            game.setGameQueue(this);
        }
    }

    public void removeGame(TournamentSingleGame game) {
        games.remove(game);
        game.setGameQueue(null);
    }

    public void clear() {
        games.forEach(g -> g.setGameQueue(null));
        games.clear();
    }
}
