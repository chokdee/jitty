package com.jmelzer.jitty.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by J. Melzer on 03.06.2016.
 * Ein satz im SPiel
 */
@Entity
public class GameSet {
    @Id
    @GeneratedValue
    private Long id;

    int points1;
    int points2;

    public GameSet(int points1, int points2) {
        this.points1 = points1;
        this.points2 = points2;
    }
}
