package com.jmelzer.jitty.model.dto;

import com.jmelzer.jitty.model.Association;
import com.jmelzer.jitty.model.Club;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Spieler
 */
public class TournamentPlayerDTO {

    List<TournamentSingleGameDTO> playedGames = new ArrayList<>();

    List<TournamentClassDTO> classes = new ArrayList<>();

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

    /**
     * count won games
     */
    private int wonGames;

    private int buchholzZahl;

    public TournamentPlayerDTO() {
    }

    public TournamentPlayerDTO(String fullName, int qttr) {
        this.fullName = fullName;
        this.qttr = qttr;
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

    public String getPeriodSinceLastGame() {
        return periodSinceLastGame;
    }

    public void setPeriodSinceLastGame(String periodSinceLastGame) {
        this.periodSinceLastGame = periodSinceLastGame;
    }

    public String getLastGameAt() {
        return lastGameAt;
    }

    public void setLastGameAt(String lastGameAt) {
        this.lastGameAt = lastGameAt;
    }

    public List<TournamentSingleGameDTO> getPlayedGames() {
        return playedGames;
    }

    public void addGame(TournamentSingleGameDTO game) {
        playedGames.add(game);
        Assert.isTrue(playedGames.size() <= 6);
    }

    public void calcWinningGames() {
        wonGames = 0;
        for (TournamentSingleGameDTO playedGame : playedGames) {
            if (playedGame.getPlayer1() == this && playedGame.getWinner() == 1) {
                wonGames++;
            } else if (playedGame.getPlayer2() == this && playedGame.getWinner() == 2) {
                wonGames++;
            }
        }

    }

    public void calcBuchholz() {
        buchholzZahl = 0;
        for (TournamentSingleGameDTO playedGame : playedGames) {
            if (playedGame.getPlayer1() == this) {
                buchholzZahl += playedGame.getPlayer2().getWonGames();
            } else if (playedGame.getPlayer2() == this) {
                buchholzZahl += playedGame.getPlayer1().getWonGames();
            }
        }
    }

    public int getWonGames() {
        return wonGames;
    }

    public int getBuchholzZahl() {
        return buchholzZahl;
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TournamentPlayerDTO playerDTO = (TournamentPlayerDTO) o;

        return fullName.equals(playerDTO.fullName);
    }

    @Override
    public String toString() {
        return "TournamentPlayerDTO{" +
                "fullName='" + fullName + '\'' +
                ", qttr=" + qttr +
                ", won=" + wonGames +
                ", buchholzZahl=" + buchholzZahl +
                ", playedAgainst=" + playedAgainst() +
                '}';
    }

    private String playedAgainst() {
        String s = "";
        for (TournamentSingleGameDTO playedGame : playedGames) {
            Assert.isTrue(playedGame.getPlayer1() == this || playedGame.getPlayer2() == this);
            if (playedGame.getPlayer1() != this && playedGame.getPlayer1() != null) {
                s += playedGame.getPlayer1().getFullName();
            } else {
                if (playedGame.getPlayer2() != null) {
                    s += playedGame.getPlayer2().getFullName();
                }
            }
            s += ",";
        }
        return s.substring(0, s.length() - 1);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean playedAgainst(TournamentPlayerDTO p) {
        for (TournamentSingleGameDTO playedGame : playedGames) {
            if (p.equals(playedGame.getPlayer1()) || p.equals(playedGame.getPlayer2())) {
                System.out.println("player " + p.getFullName() + " already played in game" + playedGame);
                return true;
            }
        }
        return false;
    }

    public void removeLastGame() {
        playedGames.remove(playedGames.size() - 1);
    }
}
