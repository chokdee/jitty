package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by J. Melzer on 01.06.2016.
 * Spieler
 */
@Entity
@Table(name = "tournament_player")
public class TournamentPlayer {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "firstname")
    private String firstName;

    @Column(nullable = false, name = "lastname")
    private String lastName;

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

    public Gender getGender() {
        return Gender.valueOf(gender);
    }

    public void setGender(Gender gender) {
        this.gender = gender.getValue();
    }
}
