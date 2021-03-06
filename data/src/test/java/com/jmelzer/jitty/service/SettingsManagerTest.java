/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.model.TableSettings;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.dto.TableSettingsDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
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

    @Test
    public void saveTableSettings() {
        Tournament t = new Tournament();
        TableSettingsDTO tableSettings = new TableSettingsDTO();
        tableSettings.setTableCount(10);
        tableSettings.addPos((short) 0, (short) 0);
        when(tournamentRepository.getOne(1L)).thenReturn(t);

        settingsManager.saveTableSettings(1L, tableSettings);

        verify(tournamentRepository).saveAndFlush(t);
    }
}