/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.utl;

import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by J. Melzer on 27.03.2018.
 */
public class ListUtilTest {

    @Test
    public void removeIfContains() {
        List<TournamentPlayerDTO> list = new ArrayList<>(Arrays.asList(
                new TournamentPlayerDTO(1L, "", 100),
                new TournamentPlayerDTO(2L, "", 102),
                new TournamentPlayerDTO(3L, "", 101)
        ));
        assertEquals(3, list.size());
        ListUtil.removeIfContains(list, new TournamentPlayer(0L, "", ""));
        assertEquals(3, list.size());
        ListUtil.removeIfContains(list, new TournamentPlayer(1L, "", ""));
        assertEquals(2, list.size());
    }
}