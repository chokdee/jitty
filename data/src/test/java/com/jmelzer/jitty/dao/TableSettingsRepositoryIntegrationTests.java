/*
 * Copyright (c) 2018.
 * J. Melzer
 */
package com.jmelzer.jitty.dao;

import com.jmelzer.jitty.model.TableSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for {@link TableSettingsRepository}.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TableSettingsRepositoryIntegrationTests {

    @Autowired
    TableSettingsRepository repository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Test
    public void getAll() {
        assertThat(repository.findAll().size(), greaterThan(0));
    }

    @Test
    public void findByTournament() {
        TableSettings settings = tournamentRepository.getOne(1L).getTableSettings();
        assertNotNull(settings);

        assertThat(settings.getTableCount(), is(8));
        assertThat(settings.getTablePositions().size(), is(8));

    }
}
