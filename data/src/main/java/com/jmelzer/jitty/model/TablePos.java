/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;

/**
 * Created by J. Melzer on 31.10.2017.
 * must be tables other weise sql will not work
 */
@Entity
@Table(name = "table_pos")
public class TablePos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private short column;

    @Column
    private short row;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SETTINGS_ID")
    private TableSettings settings;

    public TablePos() {
    }

    public TablePos(short column, short row) {
        this.column = column;
        this.row = row;
    }

    public short getColumn() {
        return column;
    }

    public short getRow() {
        return row;
    }
}
