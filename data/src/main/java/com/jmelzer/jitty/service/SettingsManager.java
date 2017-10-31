/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TableSettingsRepository;
import com.jmelzer.jitty.model.TablePos;
import com.jmelzer.jitty.model.TableSettings;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.dto.TableSettingsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by J. Melzer on 31.10.2017.
 */
@Component
public class SettingsManager {

    @Autowired
    TableSettingsRepository tableSettingsRepository;

    @Transactional(readOnly = true)
    public TableSettingsDTO getTableSettings(Tournament tournament) {
        TableSettings settings = tableSettingsRepository.findByTournament(tournament);
        TableSettingsDTO dto = new TableSettingsDTO();
        dto.setTableCount(settings.getTableCount());
        for (TablePos tablePos : settings.getTablePositions()) {
            dto.addPos(tablePos.getColumn(), tablePos.getRow());

        }
        return dto;
    }
}
