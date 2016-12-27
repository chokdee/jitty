package com.jmelzer.jitty.model;


import javax.persistence.*;

/**
 * Created by J. Melzer on 01.06.2016.
 * Verband (Stammdaten)
 */
@Entity
@Table(name = "association")
public class Association {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "shortname", unique = true)
    private String shortName;

    @Column(nullable = false, name = "longname", unique = true)
    private String longName;

    public Association() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }
}
