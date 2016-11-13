package com.jmelzer.jitty.utl;

import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;

import java.util.List;

/**
 * Created by J. Melzer on 31.10.2016.
 */
public class ListUtil {
    public static void removeIfContains(List<TournamentPlayerDTO> list, TournamentPlayer p) {
        TournamentPlayerDTO found = null;
        for (TournamentPlayerDTO tournamentPlayerDTO : list) {
            if (p != null && tournamentPlayerDTO.getId().equals(p.getId())) {
                found = tournamentPlayerDTO;
                break;
            }
        }
        if (found != null) {
            list.remove(found);
        }
    }

}
