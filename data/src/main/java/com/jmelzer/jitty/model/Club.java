/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;


import javax.persistence.*;

/**
 * Created by J. Melzer on 01.06.2016.
 * Verein (Stammdaten)
 */
@Entity
@Table(name = "club")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "click_tt_nr")
    private String clickTTNr;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Association association;

    public Club() {
    }

    public Club(String name) {
        this.name = name;
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

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public String getClickTTNr() {
        return clickTTNr;
    }

    public void setClickTTNr(String clickTTNr) {
        this.clickTTNr = clickTTNr;
    }
}
