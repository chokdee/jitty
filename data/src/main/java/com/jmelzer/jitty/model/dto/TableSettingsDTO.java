/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

import com.jmelzer.jitty.model.TablePos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 31.10.2017.
 */
public class TableSettingsDTO {
    Integer tableCount;

    List<TablePos> tablePositions = new ArrayList<>();

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
}
