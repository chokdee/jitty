/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TableSettingsRepository;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.TournamentSingleGame;
import com.jmelzer.jitty.model.dto.TableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.jmelzer.jitty.service.CopyManager.copy;

/**
 * Created by J. Melzer on 11.06.2016.
 * Hold informations about the table to play.
 */
@Component
public class TableManager {
    TreeSet<Integer> freeTables = new TreeSet<>();

    Map<Integer, TournamentSingleGame> busyTables = new HashMap<>();

    @Autowired
    TableSettingsRepository settingsRepository;

    public Set<Integer> getFreeTables() {
        return Collections.unmodifiableSet(freeTables);
    }

    /**
     * method for getting the next free table number in the list
     *
     * @return -1 if not free table avaible or no
     */
    public int pollFreeTableNo(TournamentSingleGame game) {
        if (freeTables.size() == 0) {
            return -1;
        }

        int no = freeTables.iterator().next();
        setBusyTableNo(no, game);
        return no;
    }

    public void setBusyTableNo(int no, TournamentSingleGame game) {

        busyTables.put(no, game);
        freeTables.remove(no);
    }

    public boolean isTableAvaible() {
        return freeTables.size() > 0;
    }

    @Transactional(readOnly = true)
    public List<TableDTO> getAllTables(Tournament tournament) {
        int tableCount = getTableCount(tournament);
        List<TableDTO> list = new ArrayList<>(tableCount);

        for (int i = 1; i < tableCount + 1; i++) {
            if (freeTables.contains(i)) {
                list.add(new TableDTO(i, null));
            } else if (busyTables.containsKey(i)) {
                list.add(new TableDTO(i, copy(busyTables.get(i), false)));
            }
        }

        return list;
    }

    int getTableCount(Tournament tournament) {
        return settingsRepository.findByTournament(tournament).getTableCount();
    }

    public int getBusyTableCount() {
        return busyTables.size();
    }

    public int getFreeTableCount() {
        return freeTables.size();
    }

    public void clear(int tableCount) {
//        this.tableCount = 0;
        freeTables.clear();
        setTableCount(tableCount); //refresh
        busyTables.clear();
    }

    public void pushFreeTable(TournamentSingleGame game) {
        busyTables.remove(game.getTableNo());
        freeTables.add(game.getTableNo());
    }

    public void setTableCount(int tableCount) {
        int max = maxTableNo();
        max++;
        int diff = tableCount;
        for (int i = 0; i < diff; i++) {
            freeTables.add(max++);
        }
//        this.tableCount = tableCount;

    }

    private int maxTableNo() {
        if (freeTables.size() == 0) {
            return 0;
        }
        return freeTables.last();
    }
}
