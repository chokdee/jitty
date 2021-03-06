/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by J. Melzer on 08.04.2017.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TournamentSystemType {
    //Normal Group / KO System
    GK(1, "Gruppen und KO"),
    //Schweizer System wie WTTV Cup
    AC(2, "WTTV Andro Cup"),
    VRC(3, "Rheinland Cup");

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

    public int getValue() {
        return value;
    }
}
