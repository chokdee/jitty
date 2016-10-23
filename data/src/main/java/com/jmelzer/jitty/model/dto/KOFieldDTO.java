package com.jmelzer.jitty.model.dto;

public class KOFieldDTO {
    RoundDTO round;
    private Long id;

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
