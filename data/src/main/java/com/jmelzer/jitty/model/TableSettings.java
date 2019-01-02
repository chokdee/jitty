/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 31.10.2017.
 */
@Entity
@javax.persistence.Table(name = "table_settings")
public class TableSettings {
    @Column(name = "table_count")
    Integer tableCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "settings")
    List<TablePos> tablePositions = new ArrayList<>();

//    @OneToOne(targetEntity = Tournament.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "T_ID")
//    Tournament tournament;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Integer getTableCount() {
        return tableCount;
    }

    public void setTableCount(Integer tableCount) {
        this.tableCount = tableCount;
    }

    public void addPos(short col, short row) {
        tablePositions.add(new TablePos(col, row));
    }

    public List<TablePos> getTablePositions() {
        return tablePositions;
    }

    public void setTablePositions(List<TablePos> tablePositions) {
        this.tablePositions = tablePositions;
    }
}
