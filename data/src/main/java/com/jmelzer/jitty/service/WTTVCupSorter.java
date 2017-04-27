/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;

import java.util.List;

/**
 * Created by J. Melzer on 27.04.2017.
 */
public class WTTVCupSorter {
    public static void sort(List<TournamentPlayerDTO> player) {

        player.sort((o1, o2) -> {

            int sComp = Integer.compare(o1.getWonGames(), o2.getWonGames()) * -1;

            if (sComp != 0) {
                return sComp;
            } else {
                int b = Integer.compare(o1.getBuchholzZahl(), o2.getBuchholzZahl()) * -1;
                if (b != 0) {
                    return b;
                }
                return o1.resultAgainst(o2);
//                return Integer.compare(o1.getFeinBuchholzZahl(), o2.getFeinBuchholzZahl()) * -1;
            }
        });
    }
}
