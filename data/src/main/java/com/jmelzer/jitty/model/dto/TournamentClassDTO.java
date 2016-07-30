package com.jmelzer.jitty.model.dto;


import com.jmelzer.jitty.model.GameMode;

import java.util.Date;

/**
 * Created by J. Melzer on 01.06.2016.
 * Turnier-Klasse
 */
public class TournamentClassDTO {
    private Long id;
    private String name;
    private int startTTR = 0;
    private int endTTR = 0;

    String type;

    private Date minAge;

    private Date maxAge;

    private boolean openForMen;
    private boolean openForWomen;
    Date startTime;
    /**
     * @see GameMode for values
     */
    String gameModePhase1;

    /**
     * @see GameMode for values
     */
    String gameModePhase2;

    Integer playerPerGroup;

    Integer groupCount;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getMinAge() {
        return minAge;
    }

    public void setMinAge(Date minAge) {
        this.minAge = minAge;
    }

    public Date getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Date maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isOpenForMen() {
        return openForMen;
    }

    public void setOpenForMen(boolean openForMen) {
        this.openForMen = openForMen;
    }

    public boolean isOpenForWomen() {
        return openForWomen;
    }

    public void setOpenForWomen(boolean openForWomen) {
        this.openForWomen = openForWomen;
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

    public int getStartTTR() {
        return startTTR;
    }

    public void setStartTTR(int startTTR) {
        this.startTTR = startTTR;
    }

    public int getEndTTR() {
        return endTTR;
    }

    public void setEndTTR(int endTTR) {
        this.endTTR = endTTR;
    }

    public String getGameModePhase1() {
        return gameModePhase1;
    }

    public void setGameModePhase1(String gameModePhase1) {
        this.gameModePhase1 = gameModePhase1;
    }

    public String getGameModePhase2() {
        return gameModePhase2;
    }

    public void setGameModePhase2(String gameModePhase2) {
        this.gameModePhase2 = gameModePhase2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TournamentClassDTO classDTO = (TournamentClassDTO) o;

        if (startTTR != classDTO.startTTR) {
            return false;
        }
        if (endTTR != classDTO.endTTR) {
            return false;
        }
        if (!id.equals(classDTO.id)) {
            return false;
        }
        return name.equals(classDTO.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + startTTR;
        result = 31 * result + endTTR;
        return result;
    }

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

    @Override
    public String toString() {
        return "TournamentClassDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTTR=" + startTTR +
                ", endTTR=" + endTTR +
                ", type='" + type + '\'' +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", openForMen=" + openForMen +
                ", openForWomen=" + openForWomen +
                ", startTime=" + startTime +
                ", gameModePhase1='" + gameModePhase1 + '\'' +
                ", gameModePhase2='" + gameModePhase2 + '\'' +
                ", playerPerGroup=" + playerPerGroup +
                ", groupCount=" + groupCount +
                '}';
    }
}
