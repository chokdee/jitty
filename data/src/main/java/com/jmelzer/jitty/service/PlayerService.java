package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentClassRepository;
import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 15.07.2016.
 */
@Component
public class PlayerService {
    @Resource
    TournamentPlayerRepository repository;

    @Resource
    TournamentClassRepository classRepository;

    @Transactional(readOnly = true)
    public List<TournamentPlayerDTO> findAll() {
        List<TournamentPlayer> players = repository.findAll();
        List<TournamentPlayerDTO> dtos = new ArrayList<>();
        for (TournamentPlayer player : players) {
            TournamentPlayerDTO dto = new TournamentPlayerDTO();
            BeanUtils.copyProperties(player, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional
    public TournamentPlayerDTO findOne(Long aLong) {

        TournamentPlayer tournamentPlayer = repository.findOne(aLong);
        TournamentPlayerDTO dto = new TournamentPlayerDTO();
        BeanUtils.copyProperties(tournamentPlayer, dto);
        for (TournamentClass tournamentClass : tournamentPlayer.getClasses()) {
            TournamentClassDTO classDTO = new TournamentClassDTO();
            BeanUtils.copyProperties(tournamentClass, classDTO);
            dto.addClass(classDTO);
        }
        return dto;
    }

    @Transactional
    public void delete(Long aLong) {
        repository.delete(aLong);
    }

    @Transactional
    public void save(TournamentPlayerDTO player) {
        TournamentPlayer playerDB = repository.findOne(player.getId());
        BeanUtils.copyProperties(player, playerDB, "classes");
        for (TournamentClassDTO classDTO : player.getClasses()) {
            playerDB.removeAllClasses();
            playerDB.addClass(classRepository.findOne(classDTO.getId()));
        }
        repository.save(playerDB);
    }

}
