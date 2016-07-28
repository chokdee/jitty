package com.jmelzer.jitty.model;

/**
 * Created by J. Melzer on 27.07.2016.
 */
public enum GameMode {
    GROUP("g"), KO("k"), SWISS("s"), DOUBLEKO("d");

    String value;

    GameMode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }
}
