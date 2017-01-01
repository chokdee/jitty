package com.jmelzer.jitty.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 01.11.2016.
 */
public class PlayerStatistic implements Comparable<PlayerStatistic> {
    public TournamentPlayer player;
    public int win;
    public int lose;
    public int setsWon;
    public int setsLost;
    public List<String> detailResult = new ArrayList<>();

    //todo points

    //todo C 8.5.2 Punkt- und Satzgleichheit bei mehr als 2 Spielern
//        Bei Punkt- und Satzgleichheit von mehr als zwei Spielern einer Gruppe werden nur die Ergeb-
//        nisse dieser Spieler untereinander verglichen. Kommt man bei diesem Punkt- und Satzdiffe-
//        renzvergleichen Spielern immer  noch nicht zu einem Ergebnis, so entscheidet die größere Dif-
//        ferenz zwischen gewonnenen und verlorenen Bällen. Die Spiele gegen die anderen Spieler die-
//        ser Gruppe werden beim direkten Vergleich nicht berücksichtigt.

    @Override
    public int compareTo(PlayerStatistic o) {
        int w = Integer.compare(win, o.win);
        if (w != 0) {
            return w;
        }
        int l = Integer.compare(lose, o.lose);
        if (l != 0) {
            return l;
        }
        return Integer.compare(setsRatio(), o.setsRatio());
        //todo compare balls or make it generic
    }

    int setsRatio() {
        return setsWon - setsLost;
    }

    @Override
    public String toString() {
        return "PS{" +
                "player=" + player.getFullName() +
                ", win=" + win +
                ", lose=" + lose +
                ", setsWon=" + setsWon +
                ", setsLost=" + setsLost +
                '}';
    }
}
