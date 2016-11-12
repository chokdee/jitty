package com.jmelzer.jitty.model.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by J. Melzer on 03.06.2016.
 * Ein satz im SPiel
 */
public class GameSetDTO {
    int points1;
    int points2;
    private Long id;

    public GameSetDTO() {
    }

    public GameSetDTO(int points1, int points2) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
