package com.jmelzer.jitty.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 14.08.2016.
 * For display results in the liveview
 */
public class GroupResultEntryDTO {
    int pos;
    String playerName;
    String club;
    String gameStat;
    String setStat;
    String ballStat;
    List<String> detailResult = new ArrayList<>();

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getGameStat() {
        return gameStat;
    }

    public void setGameStat(String gameStat) {
        this.gameStat = gameStat;
    }

    public String getSetStat() {
        return setStat;
    }

    public void setSetStat(String setStat) {
        this.setStat = setStat;
    }

    public String getBallStat() {
        return ballStat;
    }

    public void setBallStat(String ballStat) {
        this.ballStat = ballStat;
    }

    public List<String> getDetailResult() {
        return detailResult;
    }

    public void setDetailResult(List<String> detailResult) {
        this.detailResult = detailResult;
    }
}
