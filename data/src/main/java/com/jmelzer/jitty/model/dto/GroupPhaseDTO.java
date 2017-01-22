/*
 * Copyright (c) 2016.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 28.12.2016.
 */
public class GroupPhaseDTO extends PhaseDTO {

    /**
     * Assoc to the groups in the class.
     */
    List<TournamentGroupDTO> groups = new ArrayList<>();

    private Integer playerPerGroup;

    private Integer groupCount;

    /**
     * how many places will be qulified after group.
     */
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

    public List<TournamentGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<TournamentGroupDTO> groups) {
        this.groups = groups;
    }

    public void addGroup(TournamentGroupDTO group) {
        groups.add(group);
    }

    public String getName() {
        return "Gruppe";
    }
}
