package com.jmelzer.jitty.model;

import javax.persistence.*;

/**
 * Created by J. Melzer on 28.12.2016.
 */
@Entity
@Table(name = "ko_phase")
public class KOPhase extends Phase {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "KOFIELD_ID")
    KOField koField;

    public KOField getKoField() {
        return koField;
    }

    public void setKoField(KOField koField) {
        this.koField = koField;
    }
}
