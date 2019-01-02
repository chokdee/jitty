/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.model.TablePos;
import com.jmelzer.jitty.model.TableSettings;
import com.jmelzer.jitty.model.Tournament;
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

    @Transactional
    public void saveTableSettings(long tid, TableSettingsDTO dto) {
        Tournament t = tournamentRepository.getOne(tid);
        TableSettings settings = new TableSettings();
        settings.setTableCount(dto.getTableCount());
        for (TablePos tablePos : dto.getTablePositions()) {
            settings.addPos(tablePos.getColumn(), tablePos.getRow());
        }
        t.setTableSettings(settings);
        tournamentRepository.saveAndFlush(t);
    }
}
