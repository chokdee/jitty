package com.jmelzer.jitty;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by J. Melzer on 11.06.2016.
 * Hold informations about the table to play.
 */
public class TableManager {
    Set<Integer> freeTables = new TreeSet<>();
    Set<Integer> busyTables = new TreeSet<>();

    public Set<Integer> getFreeTables() {
        return freeTables;
    }

    public int getFreeTableNo() {
        int no = freeTables.iterator().next();
        setBusyTableNo(no);
        return no;
    }

    public void setFreeTablesNo(int no) {
        freeTables.add(no);
        busyTables.remove(no);
    }

    public void setBusyTableNo(int no) {
        busyTables.add(no);
        freeTables.remove(no);
    }

    public boolean isTableAvaible() {
        return freeTables.size() > 0;
    }
}
