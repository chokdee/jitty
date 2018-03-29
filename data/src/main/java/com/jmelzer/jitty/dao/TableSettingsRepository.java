/*
 * Copyright (c) 2018.
 * J. Melzer
 */


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.TableSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableSettingsRepository extends JpaRepository<TableSettings, Long> {


}
