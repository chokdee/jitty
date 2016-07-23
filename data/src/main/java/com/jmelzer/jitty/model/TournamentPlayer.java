package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
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
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, name = "firstname")
    private String firstName;
    @Column(nullable = false, name = "lastname")
    private String lastName;
    @ManyToOne(optional = true, cascade = CascadeType.DETACH)
    private Club club;
    @ManyToOne(optional = true, cascade = CascadeType.DETACH)
    private Association association;
    @Column(nullable = true)
    private String email;
    @Column(nullable = true, name = "mobilenumber")
    private String mobileNumber;
    @Column(nullable = true)
    private int qttr;
    @Column(nullable = true)
    private int ttr;
    @Column(nullable = true)
    private Date birthday;
    @Column(nullable = true, length = 1)
    private String gender;
    @OneToMany(cascade = CascadeType.DETACH)
    private List<TournamentSingleGame> games = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "players")
    List<TournamentClass> classes = new ArrayList<>();


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
    public String toString() {
        return "TournamentPlayer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", qttr=" + qttr +
                ", ranking=" + ranking +
                '}';
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

        return id.equals(player.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
}
