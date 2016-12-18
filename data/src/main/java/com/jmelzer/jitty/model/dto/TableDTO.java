package com.jmelzer.jitty.model.dto;

/**
 * Created by J. Melzer on 18.12.2016.
 */
public class TableDTO {
    int no;
    //maybe null if free table
    TournamentSingleGameDTO game;

    public TableDTO(int no, TournamentSingleGameDTO game) {
        this.no = no;
        this.game = game;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public TournamentSingleGameDTO getGame() {
        return game;
    }

    public void setGame(TournamentSingleGameDTO game) {
        this.game = game;
    }
}
