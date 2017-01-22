/*
 * Copyright (c) 2016.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

import com.jmelzer.jitty.model.KOField;

/**
 * Created by J. Melzer on 28.12.2016.
 */
public class KOPhaseDTO extends PhaseDTO {
    KOField koField;

    public KOField getKoField() {
        return koField;
    }

    public void setKoField(KOField koField) {
        this.koField = koField;
    }

    public String getName() {
        return "KO";
    }
}
