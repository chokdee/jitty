/*
 * Copyright (c) 2017.
 * J. Melzer
 */


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.TableSettings;
import com.jmelzer.jitty.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableSettingsRepository extends JpaRepository<TableSettings, Long> {

    TableSettings findByTournament(Tournament tournament);

}
