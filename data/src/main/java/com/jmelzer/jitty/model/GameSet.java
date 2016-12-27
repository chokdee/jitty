package com.jmelzer.jitty.model;

import javax.persistence.*;

/**
 * Created by J. Melzer on 03.06.2016.
 * Ein satz im SPiel
 */
@Entity(name = "GAME_SET")
public class GameSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    int points1;
    @Column
    int points2;

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

    public void setPoints1(int points1) {
        this.points1 = points1;
    }

    public void setPoints2(int points2) {
        this.points2 = points2;
    }
}
