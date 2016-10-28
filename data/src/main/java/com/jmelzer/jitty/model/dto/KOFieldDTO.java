package com.jmelzer.jitty.model.dto;

public class KOFieldDTO {
    RoundDTO round;
    private Long id;
    int noOfRounds;

    public int getNoOfRounds() {
        return noOfRounds;
    }

    public void setNoOfRounds(int noOfRounds) {
        this.noOfRounds = noOfRounds;
    }

    public Long getId() {
        return id;
    }

    public RoundDTO getRound() {
        return round;
    }

    public void setRound(RoundDTO round) {
        this.round = round;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
