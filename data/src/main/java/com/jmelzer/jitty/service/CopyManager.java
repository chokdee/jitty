package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentGroupDTO;
import org.springframework.beans.BeanUtils;

/**
 * Created by J. Melzer on 30.07.2016.
 *
 */
public class CopyManager {
    protected CopyManager() {
    }

    static public TournamentClassDTO copy(TournamentClass clz) {
        TournamentClassDTO dto = new TournamentClassDTO();
        BeanUtils.copyProperties(clz, dto, "groups", "players");
        for (TournamentGroup group : clz.getGroups()) {
            dto.addGroup(copy(group));
        }
        return dto;
    }

    private static TournamentGroupDTO copy(TournamentGroup group) {
        TournamentGroupDTO dto = new TournamentGroupDTO();
        BeanUtils.copyProperties(group, dto, "players");
        return dto;
    }
}
