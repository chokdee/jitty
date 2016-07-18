package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.model.TournamentPlayer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by J. Melzer on 15.07.2016.
 */
@Component
public class PlayerService {
    @Resource
    TournamentPlayerRepository repository;

    @Transactional(readOnly = true)
    public List<TournamentPlayer> findAll() {
        List<TournamentPlayer> players = repository.findAll();
        return players;
    }

    @Transactional
    public TournamentPlayer findOne(Long aLong) {
        return repository.findOne(aLong);
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
