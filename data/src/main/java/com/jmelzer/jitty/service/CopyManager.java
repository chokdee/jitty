package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentGroupDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.springframework.beans.BeanUtils;

/**
 * Created by J. Melzer on 30.07.2016.
 *
 */
public class CopyManager {

    static public void copy(TournamentClassDTO dto, TournamentClass clz, TournamentPlayerRepository playerRepository) {
        BeanUtils.copyProperties(dto, clz, "groups", "players");
        clz.getGroups().clear();
        for (TournamentGroupDTO group : dto.getGroups()) {
            clz.addGroup(copy(group, playerRepository));
        }
    }

    public static TournamentGroup copy(TournamentGroupDTO dto, TournamentPlayerRepository playerRepository) {
        TournamentGroup group = new TournamentGroup();
        BeanUtils.copyProperties(dto, group, "players");
        for (TournamentPlayerDTO tournamentPlayerDTO : dto.getPlayers()) {
            TournamentPlayer player = playerRepository.findOne(tournamentPlayerDTO.getId());
            group.addPlayer(player);
        }
        return group;
    }

    static public TournamentClassDTO copy(TournamentClass clz) {
        TournamentClassDTO dto = new TournamentClassDTO();
        BeanUtils.copyProperties(clz, dto, "groups", "players");
        for (TournamentGroup group : clz.getGroups()) {
            TournamentGroupDTO dtoGroup = copy(group);
            dto.addGroup(dtoGroup);
            for (TournamentPlayer player : group.getPlayers()) {
                dtoGroup.addPlayer(copy(player));
            }
        }

        return dto;
    }

    private static TournamentPlayerDTO copy(TournamentPlayer player) {
        TournamentPlayerDTO dto = new TournamentPlayerDTO();
        BeanUtils.copyProperties(player, dto);
        return dto;
    }

    private static TournamentGroupDTO copy(TournamentGroup group) {
        TournamentGroupDTO dto = new TournamentGroupDTO();
        BeanUtils.copyProperties(group, dto, "players");
        return dto;
    }
}
