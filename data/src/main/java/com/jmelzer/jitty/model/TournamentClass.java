/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.model;


import com.jmelzer.jitty.model.dto.TournamentClassStatus;

import javax.persistence.*;
import java.util.*;

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
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "TC_PLAYER")
    List<TournamentPlayer> players = new ArrayList<>();

    @ManyToOne(targetEntity = Tournament.class)
    @JoinColumn(name = "T_ID")
    Tournament tournament;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true, name = "START_TIME")
    Date startTime;

    /**
     * see @TournamentSystemType
     */
    @Column(name = "SYSTEM_TYPE", nullable = false)
    Integer systemType;

    //type (Einzel / Doppem / Mixed)
    //trostrunden?
    @Column(nullable = false, name = "RUNNING")
    Boolean running = false;

    @Column(nullable = false, name = "ACTIVE_PHASE")
    int activePhaseNo = -1;


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tournamentClass")
    @JoinColumn(name = "SYSTEM_ID")
    TournamentSystem system;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

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

    /** altersklasse. Values see enum AgeGroup */
    @Column(nullable = false, name = "age_group")
    private String ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private TournamentClassStatus status = TournamentClassStatus.NOTSTARTED;

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
        for (TournamentPlayer p : players) {
            if (p.getId() != null && player.getId() != null && Objects.equals(p.getId(), player.getId())) {
                return;
            }
        }
        players.add(player);
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
        if (running == null) {
            return false;
        }
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

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TournamentClass)) {
            return false;
        }

        TournamentClass that = (TournamentClass) o;

        return id.equals(that.getId());

    }

    @Override
    public String toString() {
        return "TournamentClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTTR=" + startTTR +
                ", endTTR=" + endTTR +
                ", players=" + players +
                '}';
    }

    public void removePlayer(TournamentPlayer player) {
        players.remove(player);
    }

    @Transient
    public int getPlayerCount() {
        return players.size();
    }


    public boolean finished() {
        if (getActivePhase() instanceof SwissSystemPhase) {
            SwissSystemPhase sp = (SwissSystemPhase) getActivePhase();
            if (sp.getRound() > sp.getMaxRounds()) {
                return true;
            }
            //todo create method isLastRound
            if (sp.getRound() == sp.getMaxRounds()) {
                return getActivePhase().isFinished();
            }
            return false;
        } else {
            return (getActivePhaseNo() == getPhaseCount() - 1) && getActivePhase().isFinished();
        }
    }

    @Transient
    public Phase getActivePhase() {
        if (system == null) {
            return null;
        }

        return getSystem().getPhase(activePhaseNo);
    }

    public int getActivePhaseNo() {
        return activePhaseNo;
    }

    public int getPhaseCount() {
        if (system == null) {
            return -100;
        }
        return system.getPhaseCount();
    }

    public TournamentSystem getSystem() {
        return system;
    }

    private void setSystem(TournamentSystem system) {
        this.system = system;
        system.setTournamentClass(this);
    }

    public void setActivePhaseNo(int activePhase) {
        this.activePhaseNo = activePhase;
    }

    @Transient
    public Phase getLastPhase() {
        if (system == null) {
            return null;
        }

        return getSystem().getLastPhase();
    }

    @Transient
    public List<Phase> getAllPhases() {
        if (system == null) {
            return null;
        }

        return getSystem().getPhases();
    }

    public KOField getKoField() {
        if (system != null) {
            return system.findKOField();
        }
        return null;
    }

    public void setKoField(KOField koField) {
        system.setKOField(koField);
    }

    //todo change it
    public List<TournamentGroup> getGroups() {
        if (system != null && activePhaseNo > -1) {
            return ((GroupPhase) system.getPhases().get(0)).getGroups();
        } else {
            return new ArrayList<>();
        }
    }

    public void createPhaseCombination(PhaseCombination phaseCombination) {
        if (system == null) {
            setSystem(new TournamentSystem());
        } else {
            system.clearPhases();
        }
        switch (phaseCombination) {
            case GK:
                system.addPhase(new GroupPhase());
                system.addPhase(new KOPhase());
                break;
            case SWS:
                system.addPhase(new SwissSystemPhase("Runde 1"));
                break;
            case KO:
                system.addPhase(new KOPhase());
                break;
            default:
                throw new UnsupportedOperationException("not yet implemented");
        }
    }

    public void addGroup(TournamentGroup copy) {
        ((GroupPhase) system.getPhases().get(0)).addGroup(copy);
    }


    public Phase getActualPhase() {
        if (activePhaseNo > -1) {
            return system.getPhases().get(activePhaseNo);
        } else {
            return null;
        }
    }

    public void addPhase(Phase phase) {
        system.addPhase(phase);
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public TournamentClassStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentClassStatus status) {
        this.status = status;
    }

}
