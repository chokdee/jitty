package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.model.TournamentPlayer;
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
        return dto;
    }

    @Transactional
    public void delete(Long aLong) {
        repository.delete(aLong);
    }

    @Transactional
    public TournamentPlayer save(TournamentPlayer player) {
        return repository.save(player);
    }

}
