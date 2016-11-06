package com.jmelzer.jitty.model;

import javax.persistence.*;

/**
 * Created by J. Melzer on 15.06.2016.
 */
@Entity
@Table(name = "ko_field")
public class KOField {
    @OneToOne(cascade = CascadeType.ALL)
    Round round;
    @OneToOne(cascade = CascadeType.DETACH)
    TournamentClass tournamentClass;

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private int noOfRounds;

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

    public void setNoOfRounds(int noOfRounds) {
        this.noOfRounds = noOfRounds;
    }

    public int getNoOfRounds() {
        return noOfRounds;
    }

    public TournamentClass getTournamentClass() {
        return tournamentClass;
    }

    public void setTournamentClass(TournamentClass tournamentClass) {
        this.tournamentClass = tournamentClass;
    }
}
