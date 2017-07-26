/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 28.12.2016.
 */
@Entity
@Table(name = "group_phase")
public class GroupPhase extends Phase {

    @Column(nullable = true, name = "player_per_group")
    protected Integer playerPerGroup;

    /**
     * Assoc to the groups in the class.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groupPhase", orphanRemoval = true)
    private List<TournamentGroup> groups = new ArrayList<>();

    @Column(nullable = true, name = "group_count")
    private Integer groupCount;

    /**
     * how many places will be qulified after group.
     */
    @Column(nullable = false, name = "quali_group_count")
    private int qualiGroupCount = 2;

    public Integer getPlayerPerGroup() {
        return playerPerGroup;
    }

    public void setPlayerPerGroup(Integer playerPerGroup) {
        this.playerPerGroup = playerPerGroup;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }

    public int getQualiGroupCount() {
        return qualiGroupCount;
    }

    public void setQualiGroupCount(int qualiGroupCount) {
        this.qualiGroupCount = qualiGroupCount;
    }

    public void clearGroups() {
        groups.clear();
    }

    public List<TournamentGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<TournamentGroup> groups) {
        this.groups = groups;
    }

    public void addGroup(TournamentGroup group) {
        groups.add(group);
        group.setGroupPhase(this);
    }

    @Override
    public boolean areGamesPlayed() {
        for (TournamentGroup group : groups) {
            List<TournamentSingleGame> games = group.getGames();
            for (TournamentSingleGame game : games) {
                if (game.isPlayed()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void resetGames() {
        groups.forEach(TournamentGroup::removeAllGames);
    }

    @Override
    public boolean isFinished() {
        for (TournamentGroup group : groups) {
            List<TournamentSingleGame> games = group.getGames();
            for (TournamentSingleGame game : games) {
                if (!game.isFinished()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public List<TournamentSingleGame> getAllSingleGames() {
        List<TournamentSingleGame> list = new ArrayList<>();
        for (TournamentGroup group : groups) {
            list.addAll(group.getGames());
        }
        return list;
    }
}
