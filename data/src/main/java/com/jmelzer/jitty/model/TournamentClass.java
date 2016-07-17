package com.jmelzer.jitty.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Turnier-Klasse
 */
@Entity
@Table(name = "tournament_class")
public class TournamentClass {

    /**
     * Assoc to the player in the class.
     */
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "TC_PLAYER")
    List<TournamentPlayer> players = new ArrayList<>();
    /**
     * Assoc to the groups in the class.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TC_ID")
    List<TournamentGroup> groups = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;

    //type (Einzel / Doppem / Mixed)
    //trostrunden?
    /**
     * Min TTR Wert.
     */
    @Column(nullable = true, name = "start_ttr")
    private int startTTR = 0;
    /**
     * Max TTR Wert.
     */
    @Column(nullable = true, name = "end_ttr")
    private int endTTR = 0;

    public TournamentClass(String name) {
        this.name = name;
    }

    public TournamentClass() {
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

    public List<TournamentPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void addPlayer(TournamentPlayer player) {
        players.add(player);
    }

    public List<TournamentGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<TournamentGroup> groups) {
        this.groups = groups;
    }

    public void addGroup(TournamentGroup group) {
        groups.add(group);
    }

    public int getStartTTR() {
        return startTTR;
    }


    public void setStartTTR(int startTTR) {
        this.startTTR = startTTR;
    }

    public int getEndTTR() {
        return endTTR;
    }

    public void setEndTTR(int endTTR) {
        this.endTTR = endTTR;
    }

    @Override
    public String toString() {
        return "TournamentClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTTR=" + startTTR +
                ", endTTR=" + endTTR +
                ", players=" + players +
                ", groups=" + groups +
                '}';
    }

    public void fetchAssocs() {

        for (TournamentGroup group : getGroups()) {
            group.getId();
        }
    }
}
