package com.jmelzer.jitty.model;

/**
 * Created by J. Melzer on 15.06.2016.
 */
public enum RoundType {
    R128(128), R64(64), R32(32), R16(16), QUARTER(8), HALF(4), FINAL(2);

    int value;

    RoundType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
