package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by J. Melzer on 31.07.2016.
 * test copy manager
 */
public class CopyManagerTest {
    @Test
    public void copyTC() {
        TournamentClass clz = new TournamentClass("1");
        clz.addPlayer(new TournamentPlayer());
        clz.addGroup(new TournamentGroup());
        TournamentClassDTO dto = CopyManager.copy(clz);
        assertThat(dto.getName(), is("1"));
        assertThat(dto.getGroups().size(), is(1));
    }
}
