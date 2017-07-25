/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

/**
 * Created by J. Melzer on 25.07.2017.
 */
public enum AgeGroup {
    N("Nachwuchs"), DH("Damen/Herren"), S("Senioren");

    String value;

    AgeGroup(String value) {
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
