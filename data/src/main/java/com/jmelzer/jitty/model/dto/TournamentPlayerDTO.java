package com.jmelzer.jitty.model.dto;

import com.jmelzer.jitty.model.Association;
import com.jmelzer.jitty.model.Club;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Spieler
 */
public class TournamentPlayerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private Club club;
    private Association association;
    private String email;
    private String mobileNumber;
    private int qttr;
    private int ttr;
    private Date birthday;
    private String gender;
    private String periodSinceLastGame;
    private String lastGameAt;

    List<TournamentClassDTO> classes = new ArrayList<>();

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

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
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

    public List<TournamentClassDTO> getClasses() {
        return classes;
    }

    public void addClass(TournamentClassDTO classDTO) {
        classes.add(classDTO);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPeriodSinceLastGame() {
        return periodSinceLastGame;
    }

    public void setPeriodSinceLastGame(String periodSinceLastGame) {
        this.periodSinceLastGame = periodSinceLastGame;
    }

    @Override
    public String toString() {
        return "TournamentPlayerDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", club=" + club +
                ", association=" + association +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", qttr=" + qttr +
                ", ttr=" + ttr +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", classes=" + classes +
                '}';

    }

    public String getLastGameAt() {
        return lastGameAt;
    }

    public void setLastGameAt(String lastGameAt) {
        this.lastGameAt = lastGameAt;
    }
}
