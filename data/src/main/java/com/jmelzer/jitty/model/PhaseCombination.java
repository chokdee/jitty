/*
 * Copyright (c) 2016.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

/**
 * Created by J. Melzer on 28.12.2016.
 */
public enum PhaseCombination {
    //VR Gruppe, ER KO
    GK(1),
    //VR Gruppe, ER Gruppe
    GG(2),
    //Nur KO
    KO(3),
    //VR Gruppe, ZR Gruppe,
    //Schweizer System wie WTTV Cup
    SWS(5);

    int value;

    PhaseCombination(int value) {
        this.value = value;
    }

    public static PhaseCombination enumOf(int code) {
        for (PhaseCombination typе : PhaseCombination.values()) {
            if (typе.value == code) {
                return typе;
            }
        }
        return null;
    }
}
