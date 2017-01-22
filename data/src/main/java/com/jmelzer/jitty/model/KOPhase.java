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

    @Override
    public boolean areGamesPlayed() {
        for (TournamentSingleGame game : koField.getRound().getGames()) {
            if (game.isPlayed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void resetGames() {
        int nr = koField.getNoOfRounds();
        Round round = koField.getRound();
        for (int i = 0; i < nr; i++) {
            round.removeAllGames();
            round = round.getNextRound();
        }
    }

    @Override
    public boolean isFinished() {
        if (koField == null) {
            return false;
        }

        int nr = koField.getNoOfRounds();
        Round round = koField.getRound();
        for (int i = 0; i < nr; i++) {
            for (TournamentSingleGame game : round.getGames()) {
                if (!game.isFinished()) {
                    return false;
                }
            }
            round = round.getNextRound();
        }
        return true;
    }
}
