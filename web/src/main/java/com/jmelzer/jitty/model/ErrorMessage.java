/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.model;

/**
 * Created by J. Melzer on 10.07.2016.
 */
public class ErrorMessage {
    String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
