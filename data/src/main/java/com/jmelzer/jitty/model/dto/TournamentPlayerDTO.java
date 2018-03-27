/*
 * Copyright (c) 2018.
 * J. Melzer
 */

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

    public static TournamentPlayerDTO BYE = new TournamentPlayerDTO(0L, "FREI LOS", 0);

    List<TournamentSingleGameDTO> playedGames = new ArrayList<>();

    List<TournamentClassDTO> classes = new ArrayList<>();

    private Long id;

    private String importId;

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

    private int feinBuchholzZahl;

    private Boolean suspended;

    private String suspendedText;

    private boolean freilos;

    private int gamesCount = 0;

    public TournamentPlayerDTO() {
    }

    public TournamentPlayerDTO(Long id, String fullName, int qttr) {
        this.id = id;
        this.fullName = fullName;
        this.qttr = qttr;
    }

    public TournamentPlayerDTO(String importId, String fullName, int qttr) {
        this.importId = importId;
        this.fullName = fullName;
        this.qttr = qttr;
    }

    public String getImportId() {
        return importId;
    }

    public void setImportId(String importId) {
        this.importId = importId;
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
    }

    public void calcWinningGames() {
        wonGames = 0;
        for (TournamentSingleGameDTO playedGame : playedGames) {
            if (playedGame.getPlayer1().getId().equals(id) && playedGame.getWinner() == 1) {
                wonGames++;
            } else if (playedGame.getPlayer2().getId().equals(id) && playedGame.getWinner() == 2) {
                wonGames++;
            }
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void calcBuchholz(List<TournamentPlayerDTO> player) {
        buchholzZahl = 0;
        if (playedGames == null || playedGames.isEmpty()) {
            return;
        }
        for (TournamentSingleGameDTO playedGame : playedGames) {
            if (playedGame.getPlayer1().getId().equals(id)) {
                buchholzZahl += getWonGames(player, playedGame.getPlayer2().getId());
            } else if (playedGame.getPlayer2().getId().equals(id)) {
                buchholzZahl += getWonGames(player, playedGame.getPlayer1().getId());
            }
        }
//        System.out.println("buchholzZahl = " + buchholzZahl);
    }

    /**
     * workaround method, cause the player in the playedGames are not fully filled
     *
     * @param player to be searched for
     * @param id     to be searched
     * @return 0 or won games
     */
    private int getWonGames(List<TournamentPlayerDTO> player, Long id) {
        for (TournamentPlayerDTO tournamentPlayerDTO : player) {
            if (tournamentPlayerDTO.getId().equals(id)) {
                return tournamentPlayerDTO.getWonGames();
            }
        }
        return 0;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void calcFeinBuchholz(List<TournamentPlayerDTO> player) {
        feinBuchholzZahl = 0;
        if (playedGames == null || playedGames.isEmpty()) {
            return;
        }
        for (TournamentSingleGameDTO playedGame : playedGames) {
            if (playedGame.getPlayer1().getId().equals(id)) {
                feinBuchholzZahl += getBuchholz(player, playedGame.getPlayer2().getId());
            } else if (playedGame.getPlayer2().getId().equals(id)) {
                feinBuchholzZahl += getBuchholz(player, playedGame.getPlayer1().getId());
            }
        }
//        System.out.println("feinBuchholzZahl = " + feinBuchholzZahl);
    }

    private int getBuchholz(List<TournamentPlayerDTO> player, Long id) {
        for (TournamentPlayerDTO tournamentPlayerDTO : player) {
            if (tournamentPlayerDTO.getId().equals(id)) {
                return tournamentPlayerDTO.getBuchholzZahl();
            }
        }
        return 0;
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
                "id='" + id + '\'' +
                "fullName='" + fullName + '\'' +
                ", qttr=" + qttr +
                ", won=" + wonGames +
                ", buchholzZahl=" + buchholzZahl +
                ", playedAgainst=" + playedAgainst() +
                ", #played=" + playedGames.size() +
                ", freilos=" + freilos +
                '}';
    }

    private String playedAgainst() {
        String s = "";
        if (playedGames.isEmpty()) {
            return s;
        }
        for (TournamentSingleGameDTO playedGame : playedGames) {
            Assert.isTrue(playedGame.getPlayer1().getId().equals(id) || playedGame.getPlayer2().getId().equals(id));
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
//                System.out.println("player " + p.getFullName() + " already played in game" + playedGame);
                return true;
            }
        }
        return false;
    }

    public void removeLastGame() {
        playedGames.remove(playedGames.size() - 1);
    }

    public void clearGames() {
        this.playedGames.clear();
    }

    public int getFeinBuchholzZahl() {
        return feinBuchholzZahl;
    }


    public Boolean getSuspended() {
        return suspended;
    }

    public int resultAgainst(TournamentPlayerDTO p2) {
        for (TournamentSingleGameDTO playedGame : playedGames) {
            if (playedGame.getPlayer1().getId().equals(p2.getId())) {
                if (playedGame.getWinner() == 1) {
                    return -1;
                } else {
                    return 1;
                }
            }
            if (playedGame.getPlayer2().getId().equals(p2.getId())) {
                if (playedGame.getWinner() == 1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
        return 0;
    }

    public boolean isSuspended() {
        return suspended != null && suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public String getSuspendedText() {
        return suspendedText;
    }

    public void setSuspendedText(String suspendedText) {
        this.suspendedText = suspendedText;
    }

    public void setFreilos(boolean freilos) {
        this.freilos = freilos;
    }

    public boolean hasFreilos() {
        return freilos;
    }

    public void inkrementGamesCount() {
        gamesCount++;
    }

    public int getGamesCount() {
        return gamesCount;
    }
}
