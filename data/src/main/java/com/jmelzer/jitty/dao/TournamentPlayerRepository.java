/*
 * Copyright (c) 2017.
 * J. Melzer
 */


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentPlayer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentPlayerRepository extends JpaRepository<TournamentPlayer, Long> {
    Page<TournamentPlayer> findAll(Pageable pageable);

    TournamentPlayer findByLastName(String lastName);
    List<TournamentPlayer> findByLastNameAndFirstName(String lastName, String firstName);
    List<TournamentPlayer> findByClasses(List<TournamentClass> list);

}
