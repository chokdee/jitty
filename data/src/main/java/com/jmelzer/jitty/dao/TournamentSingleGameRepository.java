/*
 * Copyright (c) 2017.
 * J. Melzer
 */


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.TournamentSingleGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentSingleGameRepository extends JpaRepository<TournamentSingleGame, Long> {
    List<TournamentSingleGame> findByPlayedAndTidOrderByEndTimeDesc(boolean played, long tid);
}
