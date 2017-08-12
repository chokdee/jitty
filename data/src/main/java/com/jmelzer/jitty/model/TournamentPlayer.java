/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.*;

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

    //some click-tt attributes
    //internal-nr
    @Column(name = "click_tt_internal_nr")
    String clickTTInternalNr;

    @Column(name = "click_tt_licence_nr")
    String clickTTLicenceNr;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "firstname")
    private String firstName;

    @Column(nullable = false, name = "lastname")
    private String lastName;

    @ManyToOne(optional = true, cascade = CascadeType.DETACH)
    private Club club;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Tournament tournament;

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

    public void removeGame(TournamentSingleGame game) {
        games.remove(game);
    }

    public List<TournamentSingleGame> getGames() {
        return games;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        if (this.tournament != null && tournament != null && !this.tournament.getId().equals(tournament.getId())) {
            throw new RuntimeException("Player cannot assign to another tournament");
        }
        this.tournament = tournament;
        if (tournament != null) {
            this.tournament.addPlayer(this);
        }
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

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public String getClickTTInternalNr() {
        return clickTTInternalNr;
    }

    public void setClickTTInternalNr(String clickTTInternalNr) {
        this.clickTTInternalNr = clickTTInternalNr;
    }

    public String getClickTTLicenceNr() {
        return clickTTLicenceNr;
    }

    public void setClickTTLicenceNr(String clickTTLicenceNr) {
        this.clickTTLicenceNr = clickTTLicenceNr;
    }

    public String getBirthdayYearAsString() {
        if (birthday == null) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(birthday);
        return "" + calendar.get(Calendar.YEAR);
    }
}
