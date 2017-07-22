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
public enum Distance {
    KILOMETER("km", 1000),
    MILE("miles", 1609.34),
    METER("meters", 1),
    INCH("inches", 0.0254),
    CENTIMETER("cm", 0.01),
    MILLIMETER("mm", 0.001);
    @JsonProperty(required = false)
    private final double meters;

    @JsonProperty(required = false)
    private String unit;

    Distance(String unit, double meters) {
        this.unit = unit;
        this.meters = meters;
    }

    // standard getters and setters
}