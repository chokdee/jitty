package com.jmelzer.jitty.model.dto;


import java.util.Date;

/**
 * Created by J. Melzer on 01.06.2016.
 * Turnier-Klasse
 */
public class TournamentClassDTO {
    private Long id;

    private String type;

    private Date startTime;

    private boolean running;

    private TournamentClassStatus status;

    private TournamentSystemDTO system;

    private String name;

    private int startTTR = 0;

    private int endTTR = 0;

    private Date minAge;

    private Date maxAge;

    private boolean openForMen;

    private boolean openForWomen;

    int activePhaseNo = -1;

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + startTTR;
        result = 31 * result + endTTR;
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

        TournamentClassDTO classDTO = (TournamentClassDTO) o;

        if (startTTR != classDTO.startTTR) {
            return false;
        }
        if (endTTR != classDTO.endTTR) {
            return false;
        }
        if (!id.equals(classDTO.id)) {
            return false;
        }
        return name.equals(classDTO.name);

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public TournamentClassStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentClassStatus status) {
        this.status = status;
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

    public TournamentSystemDTO getSystem() {
        return system;
    }

    public void setSystem(TournamentSystemDTO system) {
        this.system = system;
    }

    public int getActivePhaseNo() {
        return activePhaseNo;
    }

    public void setActivePhaseNo(int activePhaseNo) {
        this.activePhaseNo = activePhaseNo;
    }
}
