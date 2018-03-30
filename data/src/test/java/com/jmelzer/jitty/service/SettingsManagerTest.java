/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.model.TableSettings;
import com.jmelzer.jitty.model.Tournament;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * Created by J. Melzer on 30.03.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class SettingsManagerTest {

    @InjectMocks
    SettingsManager settingsManager = new SettingsManager();

    @Mock
    TournamentRepository tournamentRepository;

    @Test
    public void getTableSettings() {
        Tournament t = new Tournament();
        TableSettings tableSettings = new TableSettings();
        tableSettings.setTableCount(7);
        t.setTableSettings(tableSettings);
        when(tournamentRepository.getOne(1L)).thenReturn(t);
        settingsManager.getTableSettings(1L);
    }
}