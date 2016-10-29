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

    public static RoundType fromValue(int i) {
        for (RoundType roundType : RoundType.values()) {
            if (roundType.getValue() == i)
                return roundType;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        switch (this) {
            case R128:
                return "Letzte 128";
            case R64:
                return "Letzte 64";
            case R32:
                return "Letzte 32";
            case R16:
                return "Achtelfinale";
            case QUARTER:
                return "Viertelfinale";
            case HALF:
                return "Halbfinale";
            case FINAL:
                return "Finale";
        }
        return "unkown";
    }
}
