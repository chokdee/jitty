package com.jmelzer.jitty.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Turnier-Klasse
 */
@Entity
@Table(name = "tournament_class")
public class TournamentClass {
    @Id
    @GeneratedValue
    private Long id;
    /**
     * Assoc to the player in the class.
     */
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "TC_PLAYER")
    List<TournamentPlayer> players = new ArrayList<>();
    /**
     * Assoc to the groups in the class.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tournamentClass")
    List<TournamentGroup> groups = new ArrayList<>();

    @Column(nullable = false, name = "name")
    private String name;

    @ManyToOne(targetEntity = Tournament.class)
    @JoinColumn(name = "T_ID")
    Tournament tournament;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true, name = "start_time")
    Date startTime;

    @Column(nullable = true)
    String type;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true, name = "min_age")
    private Date minAge;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true, name = "max_age")
    private Date maxAge;

    @Column(nullable = false, name = "open_for_men")
    private boolean openForMen;
    @Column(nullable = false, name = "open_for_women")
    private boolean openForWomen;

    @Column(nullable = false, name = "running")
    Boolean running = false;

    @Column(nullable = true, name = "phase")
    Integer phase = 0;


    /**
     * @see GameMode for values
     */
    @Column(nullable = true, name = "game_mode_phase_1", length = 1)
    String gameModePhase1;

    /**
     * @see GameMode for values
     */
    @Column(nullable = true, name = "game_mode_phase_2", length = 1)
    String gameModePhase2;

    @Column(nullable = true, name = "player_per_group")
    Integer playerPerGroup;

    @Column(nullable = true, name = "group_count")
    Integer groupCount;

    @OneToOne
    KOField koField;

    public TournamentClass(String name) {
        this.name = name;
    }

    public TournamentClass() {
    }

    public KOField getKoField() {
        return koField;
    }

    public void setKoField(KOField koField) {
        this.koField = koField;
    }

    public Integer getPlayerPerGroup() {
        return playerPerGroup;
    }

    public void setPlayerPerGroup(Integer playerPerGroup) {
        this.playerPerGroup = playerPerGroup;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }

    public String getGameModePhase1() {
        return gameModePhase1;
    }

    public void setGameModePhase1(String gameModePhase1) {
        this.gameModePhase1 = gameModePhase1;
    }

    public String getGameModePhase2() {
        return gameModePhase2;
    }

    public void setGameModePhase2(String gameModePhase2) {
        this.gameModePhase2 = gameModePhase2;
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
        group.setTournamentClass(this);
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

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TournamentClass that = (TournamentClass) o;

        return id.equals(that.id);

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getMinAge() {
        return minAge;
    }

    public void setMinAge(Date minAge) {
        this.minAge = minAge;
    }

    public Date getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Date maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isOpenForMen() {
        return openForMen;
    }

    public void setOpenForMen(boolean openForMen) {
        this.openForMen = openForMen;
    }

    public boolean isOpenForWomen() {
        return openForWomen;
    }

    public void setOpenForWomen(boolean openForWomen) {
        this.openForWomen = openForWomen;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void removePlayer(TournamentPlayer player) {
        players.remove(player);
    }

    @Transient
    public int getPlayerCount() {
        return players.size();
    }

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }
}
