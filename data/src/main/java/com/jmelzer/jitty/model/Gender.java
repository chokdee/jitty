package com.jmelzer.jitty.model;

/**
 * Created by J. Melzer on 01.06.2016.
 * enum for woman and man.
 */
public enum Gender {
    M("m"), W("w");

    String value;

    Gender(String value) {
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
