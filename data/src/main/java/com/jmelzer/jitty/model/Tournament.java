package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Turnier
 */
@Entity
@Table(name = "tournament")
public class Tournament {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;


    /** Assoc to the tournament classes. */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "T_ID")
    List<TournamentClass> classes = new ArrayList<>();


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

    public List<TournamentClass> getClasses() {
        return Collections.unmodifiableList(classes);
    }

    public void addClass(TournamentClass cl) {
        classes.add(cl);
    }
}
