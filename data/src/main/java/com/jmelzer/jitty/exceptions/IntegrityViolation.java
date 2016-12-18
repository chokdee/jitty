package com.jmelzer.jitty.exceptions;

/**
 * Created by J. Melzer on 04.07.2016.
 * Exception for integrity violations.
 */
public class IntegrityViolation extends Exception {

    private static final long serialVersionUID = 8311288031287025787L;

    public IntegrityViolation(String message) {
        super(message);
    }
}
