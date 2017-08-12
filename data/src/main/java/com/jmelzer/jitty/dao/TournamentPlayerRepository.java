/*
 * Copyright (c) 2017.
 * J. Melzer
 */


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentPlayer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Long> {
    Page<TournamentPlayer> findAll(Pageable pageable);

    TournamentPlayer findByLastName(String lastName);

    List<TournamentPlayer> findByLastNameAndFirstNameAndTournament(String lastName, String firstName, Tournament tournament);

    List<TournamentPlayer> findByClasses(List<TournamentClass> list);

    @Modifying
    @Query(value = "DELETE FROM TG_PLAYER WHERE PLAYER_ID = :id", nativeQuery = true)
    void removeFromGroups(@Param("id") long id);

    @Query(value = "SELECT COUNT(*) FROM TOURNAMENT_SINGLE_GAME WHERE PLAYER1_ID = :id OR PLAYER2_ID = :id", nativeQuery = true)
    int countGames(@Param("id") long id);
}
