package com.jmelzer.jitty.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Verein (Stammdaten)
 */
@Entity
@Table(name = "club")
public class Club {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "association")
    private String association;

    public Club() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }
}
