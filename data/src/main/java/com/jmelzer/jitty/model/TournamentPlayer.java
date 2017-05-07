/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Spieler
 */
@Entity
@Table(name = "tournament_player")
public class TournamentPlayer {

    public static TournamentPlayer BYE = new TournamentPlayer(0L, "FREI", "LOS");

    public transient int ranking = 0;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "players")
    List<TournamentClass> classes = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "firstname")
    private String firstName;

    @Column(nullable = false, name = "lastname")
    private String lastName;

    @ManyToOne(optional = true, cascade = CascadeType.DETACH)
    private Club club;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "players")
    private List<Tournament> tournaments = new ArrayList<>();

    @ManyToOne(optional = true, cascade = CascadeType.DETACH)
    private Association association;

    @Column
    private String email;

    @Column(nullable = true, name = "mobilenumber")
    private String mobileNumber;

    @Column(nullable = true)
    private int qttr;

    @Column
    private int ttr;

    @Temporal(TemporalType.DATE)
    @Column
    private Date birthday;

    @Column(length = 1)
    private String gender;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_game_at")
    private Date lastGameAt;

    @Column
    private Boolean suspended = false;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "GAME_TO_PLAYER",
            joinColumns = @JoinColumn(name = "GAME_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PLAYER_ID", referencedColumnName = "ID"))
    private List<TournamentSingleGame> games = new ArrayList<>();


    public TournamentPlayer() {
    }

    public TournamentPlayer(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getQttr() {
        return qttr;
    }

    public void setQttr(int qttr) {
        this.qttr = qttr;
    }

    public int getTtr() {
        return ttr;
    }

    public void setTtr(int ttr) {
        this.ttr = ttr;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TournamentPlayer player = (TournamentPlayer) o;

        if (id != null ? !id.equals(player.id) : player.id != null) {
            return false;
        }
        if (firstName != null ? !firstName.equals(player.firstName) : player.firstName != null) {
            return false;
        }
        return lastName != null ? lastName.equals(player.lastName) : player.lastName == null;

    }

    @Override
    public String toString() {
        return "TournamentPlayer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", qttr=" + qttr +
                ", ranking=" + ranking +
                '}';
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addGame(TournamentSingleGame game) {
        if (!games.contains(game)) {
            games.add(game);
        }
    }

    public List<TournamentSingleGame> getGames() {
        return games;
    }


    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void addTournament(Tournament tournament) {
        this.tournaments.add(tournament);
        tournament.addPlayer(this);
    }

    public List<TournamentClass> getClasses() {
        return classes;
    }

    public void setClasses(List<TournamentClass> classes) {

        this.classes = classes;
        for (TournamentClass aClass : classes) {
            aClass.addPlayer(this);
        }
    }

    public void addClass(TournamentClass tournamentClass) {
        classes.add(tournamentClass);
        tournamentClass.addPlayer(this);
    }

    public void removeAllClasses() {
        for (TournamentClass aClass : classes) {
            aClass.removePlayer(this);
        }
        classes.clear();
    }

    public Date getLastGameAt() {
        return lastGameAt;
    }

    public void setLastGameAt(Date lastGameAt) {
        this.lastGameAt = lastGameAt;
    }

    public void removeAllTournaments() {
        tournaments.clear();
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }
}
