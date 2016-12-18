package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.TournamentSingleGame;
import com.jmelzer.jitty.model.dto.TableDTO;
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
    int tableCount = 0;

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
        game.setTableNo(no);
        busyTables.put(no, game);
        freeTables.remove(no);
    }

    public boolean isTableAvaible() {
        return freeTables.size() > 0;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        int max = maxTableNo();
        max++;
        int diff = tableCount - this.tableCount;
        for (int i = 0; i < diff; i++) {
            freeTables.add(max++);
        }
        this.tableCount = tableCount;

    }

    private int maxTableNo() {
        if (freeTables.size() == 0) {
            return 0;
        }
        return freeTables.last();
    }

    public int getBusyTableCount() {
        return busyTables.size();
    }

    public int getFreeTableCount() {
        return freeTables.size();
    }

    @Transactional(readOnly = true)
    public List<TableDTO> getAllTables() {
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

    public void pushFreeTable(TournamentSingleGame game) {
        busyTables.remove(game.getTableNo());
        freeTables.add(game.getTableNo());
    }
}
