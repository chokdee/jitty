/* 
* Copyright (C) allesklar.com AG
* All rights reserved.
*
* Author: juergi
* Date: 27.05.12 
*
*/


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.TournamentSingleGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentSingleGameRepository extends JpaRepository<TournamentSingleGame, Long> {
    List<TournamentSingleGame> findByPlayedOrderByEndTimeDesc(boolean played);
}
