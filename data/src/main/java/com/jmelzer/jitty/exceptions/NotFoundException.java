/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.exceptions;

/**
 * Created by J. Melzer on 01.04.2018.
 */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3606236940627750076L;

    public NotFoundException(String message) {
        super(message);
    }
}
