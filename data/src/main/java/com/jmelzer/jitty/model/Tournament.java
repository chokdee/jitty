package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by J. Melzer on 01.06.2016.
 * Turnier
 */
@Entity
@Table(name = "tournament")
public class Tournament {
    /**
     * Assoc to the tournament classes.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tournament")
    List<TournamentClass> classes = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "start_date")
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "end_date")
    private Date endDate;

    @Column(nullable = false, name = "table_count")
    private int tableCount = 8;

    @Column(nullable = false, name = "running")
    private boolean running = true;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<TournamentClass> getClasses() {
        return Collections.unmodifiableList(classes);
    }

    public void addClass(TournamentClass cl) {
        classes.add(cl);
        cl.setTournament(this);
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", classes=" + classes +
                '}';
    }

    public void removeClass(TournamentClass tc) {
        classes.remove(tc);
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }
}
