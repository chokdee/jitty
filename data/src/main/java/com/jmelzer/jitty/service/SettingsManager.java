/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.model.TablePos;
import com.jmelzer.jitty.model.TableSettings;
import com.jmelzer.jitty.model.dto.TableSettingsDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by J. Melzer on 31.10.2017.
 */
@Component
public class SettingsManager {

    @Resource
    TournamentRepository tournamentRepository;

    @Transactional(readOnly = true)
    public TableSettingsDTO getTableSettings(long tid) {
        TableSettings settings = tournamentRepository.getOne(tid).getTableSettings();
        TableSettingsDTO dto = new TableSettingsDTO();
        dto.setTableCount(settings.getTableCount());
        for (TablePos tablePos : settings.getTablePositions()) {
            dto.addPos(tablePos.getColumn(), tablePos.getRow());

        }
        return dto;
    }
}
