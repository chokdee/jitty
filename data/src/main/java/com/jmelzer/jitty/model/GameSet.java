package com.jmelzer.jitty.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by J. Melzer on 03.06.2016.
 * Ein satz im SPiel
 */
@Entity
public class GameSet {
    @Column
    int points1;
    @Column
    int points2;
    @Id
    @GeneratedValue
    private Long id;

    public GameSet() {
    }

    public GameSet(int points1, int points2) {
        this.points1 = points1;
        this.points2 = points2;
    }

    public int getPoints1() {
        return points1;
    }

    public int getPoints2() {
        return points2;
    }
}
