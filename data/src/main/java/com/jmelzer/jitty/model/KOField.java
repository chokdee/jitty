package com.jmelzer.jitty.model;

import javax.persistence.*;

/**
 * Created by J. Melzer on 15.06.2016.
 */
@Entity
@Table(name = "ko_field")
public class KOField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NO_OF_ROUNDS")
    private int noOfRounds;

    @OneToOne(cascade = CascadeType.ALL)
    private Round round;

    public Long getId() {
        return id;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
        round.setKoField(this);
    }

    public int getNoOfRounds() {
        return noOfRounds;
    }

    public void setNoOfRounds(int noOfRounds) {
        this.noOfRounds = noOfRounds;
    }

}
