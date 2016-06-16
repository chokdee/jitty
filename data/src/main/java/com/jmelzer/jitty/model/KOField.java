package com.jmelzer.jitty.model;

import javax.annotation.processing.RoundEnvironment;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 15.06.2016.
 */
@Entity
@Table(name = "ko_field")
public class KOField {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    Round round;

    public void setRound(Round round) {
        this.round = round;
    }

    public Long getId() {
        return id;
    }

    public Round getRound() {
        return round;
    }

}
