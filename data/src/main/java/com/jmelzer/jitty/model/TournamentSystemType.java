/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by J. Melzer on 08.04.2017.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TournamentSystemType {
    //Normal Group / KO System
    GK(1, "Gruppen und KO"),
    //Schweizer System wie WTTV Cup
    SWS(2, "Schweizer System (Cup Systeme)");

    @JsonProperty
    int value;
    @JsonProperty
    String label;

    TournamentSystemType(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static TournamentSystemType enumOf(int code) {
        for (TournamentSystemType typе : TournamentSystemType.values()) {
            if (typе.value == code) {
                return typе;
            }
        }
        return null;
    }
}
