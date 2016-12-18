package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.TournamentSingleGame;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by J. Melzer on 18.12.2016.
 */
public class TableManagerTest {
    TableManager tableManager = new TableManager();

    @Test
    public void testFreeBusy() {
        TournamentSingleGame game = new TournamentSingleGame();
        tableManager.setTableCount(0);
        assertThat(tableManager.getBusyTableCount(), is(0));
        assertThat(tableManager.getFreeTableCount(), is(0));
        assertThat(tableManager.pollFreeTableNo(game), is(-1));

        tableManager.setTableCount(8);
        assertThat(tableManager.getFreeTableCount(), is(8));
        assertThat(tableManager.pollFreeTableNo(game), is(1));
        assertThat(tableManager.getBusyTableCount(), is(1));
        assertThat(tableManager.isTableAvaible(), is(true));
        assertThat(tableManager.getFreeTableCount(), is(7));

        int j = 7;
        for (int i = 2; i < 9; i++, j--) {
            assertThat(tableManager.getFreeTableCount(), is(j));
            assertThat(tableManager.pollFreeTableNo(game), is(i));
            assertThat(tableManager.getBusyTableCount(), is(i));
            assertThat(tableManager.getFreeTableCount(), is(j - 1));
        }
        assertThat(tableManager.isTableAvaible(), is(false));
    }
}
