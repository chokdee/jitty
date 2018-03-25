/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.*;
import com.jmelzer.jitty.exceptions.IntegrityViolation;
import com.jmelzer.jitty.model.Association;
import com.jmelzer.jitty.model.Club;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.model.xml.clicktt.Competition;
import com.jmelzer.jitty.model.xml.clicktt.Person;
import com.jmelzer.jitty.model.xml.clicktt.Player;
import com.jmelzer.jitty.model.xml.clicktt.Tournament;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by J. Melzer on 15.07.2016.
 */
@Component
public class PlayerService {
    static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy");

    @Resource
    XMLImporter xmlImporter;

    @Resource
    TournamentPlayerRepository repository;

    @Resource
    ClubRepository clubRepository;

    @Resource
    AssociationRepository associationRepository;

    @Resource
    TournamentClassRepository classRepository;

    @Resource
    TournamentRepository tournamentRepository;

    @Transactional(readOnly = true)
    public List<TournamentPlayerDTO> findAll(Long actualTournament) {
        Collection<TournamentPlayer> players = tournamentRepository.getOne(actualTournament).getPlayers();
        List<TournamentPlayerDTO> dtos = new ArrayList<>();
        for (TournamentPlayer player : players) {
            TournamentPlayerDTO dto = new TournamentPlayerDTO();
            BeanUtils.copyProperties(player, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional
    public TournamentPlayerDTO getOne(Long aLong) {

        TournamentPlayer tournamentPlayer = repository.getOne(aLong);
        TournamentPlayerDTO dto = new TournamentPlayerDTO();
        BeanUtils.copyProperties(tournamentPlayer, dto, "classes");
        for (TournamentClass tournamentClass : tournamentPlayer.getClasses()) {
            TournamentClassDTO classDTO = new TournamentClassDTO();
            BeanUtils.copyProperties(tournamentClass, classDTO, "groups");
            dto.addClass(classDTO);
        }
        return dto;
    }

    @Transactional
    public void delete(Long aLong) throws IntegrityViolation {
        TournamentPlayer player = repository.getOne(aLong);
        if (player == null) {
            throw new IntegrityViolation("Der Spieler existiert nicht.");
        }
        if (repository.countGames(aLong) > 0) {
            throw new IntegrityViolation("Es wurden bereits Spiele gespielt, der Spieler kann nicht mehr gel\u00F6scht werden");
        }
        if (player.getTournament() != null) {
            player.getTournament().removePlayer(player);
            tournamentRepository.save(player.getTournament());
        }
        if (player.getClasses() != null) {
            for (TournamentClass tournamentClass : player.getClasses()) {
                tournamentClass.removePlayer(player);
                classRepository.save(tournamentClass);
            }
        }
        repository.removeFromGroups(aLong);
        repository.delete(player);
    }

    @Transactional
    public void save(TournamentPlayerDTO player, Long tid) {
        TournamentPlayer playerDB = null;
        com.jmelzer.jitty.model.Tournament actualTournament = tournamentRepository.getOne(tid);
        if (player.getId() == null) {
            playerDB = new TournamentPlayer();
        } else {
            playerDB = repository.getOne(player.getId());
        }
        BeanUtils.copyProperties(player, playerDB, "classes");
        for (TournamentClassDTO classDTO : player.getClasses()) {
            playerDB.removeAllClasses();
            playerDB.addClass(classRepository.getOne(classDTO.getId()));
        }
        playerDB.setTournament(actualTournament);
        repository.save(playerDB);
    }

    @Transactional
    public int importPlayerFromClickTT(InputStream istream, Long tid, Boolean assignWhileImport) {
        Tournament clickTTTournament = xmlImporter.parseClickTTPlayerExport(istream);
        int count = 0;

        com.jmelzer.jitty.model.Tournament actualTournament = tournamentRepository.getOne(tid);
        actualTournament.setClickTTId(clickTTTournament.getTournamentId());
        TournamentClass tc = null;
        if (assignWhileImport) {
            tc = actualTournament.getClasses().get(0);
        }
        List<TournamentPlayer> modifiedPlayer = new ArrayList<>();

        //todo validate tournament
        for (Competition competition : clickTTTournament.getCompetition()) {
            for (Player clickTTPlayer : competition.getPlayers().getPlayer()) {

                Person person = clickTTPlayer.getPerson().get(0);
                List<TournamentPlayer> dbPlayers = repository.findByLastNameAndFirstNameAndTournament(person.getLastname(),
                        person.getFirstname(), actualTournament);

                if (dbPlayers.size() == 0 || dbPlayers.size() > 1) {
                    TournamentPlayer dbP = createDbPlayer(clickTTPlayer);
                    count++;
                    modifiedPlayer.add(dbP);

                } else if (dbPlayers.size() == 1) {
                    TournamentPlayer dbP = dbPlayers.get(0);
                    if (compareClub(dbP, clickTTPlayer)) {
                        merge(dbP, clickTTPlayer);
                    } else {
                        dbP = createDbPlayer(clickTTPlayer);
                    }
                    count++;
                    modifiedPlayer.add(dbP);

                }
            }
        }
        for (TournamentPlayer player : modifiedPlayer) {
            player = repository.saveAndFlush(player);
            player.setTournament(actualTournament);
            if (tc != null) {
                tc.addPlayer(player);
                classRepository.saveAndFlush(tc);
            }
        }
        tournamentRepository.saveAndFlush(actualTournament);
        return count;
    }

    private TournamentPlayer createDbPlayer(Player clickTTPlayer) {
        Person person = clickTTPlayer.getPerson().get(0);
        TournamentPlayer tp = new TournamentPlayer();
        tp.setFirstName(person.getFirstname());
        tp.setLastName(person.getLastname());
        tp.setGender(person.getSex().equals("0") ? "w" : "m");
        tp.setClickTTLicenceNr(person.getLicenceNr());
        tp.setClickTTInternalNr(person.getInternalNr());
        Club club = clubRepository.findByName(person.getClubName());
        if (club == null) {
            club = new Club(person.getClubName());
            club.setClickTTNr(person.getClubNr());
        }
        if (club.getClickTTNr() == null) {
            club.setClickTTNr(person.getClubNr());
        }
        if (club.getAssociation() == null) {
            Association association = associationRepository.findByShortNameIgnoreCase(person.getClubFederationNickname());
            club.setAssociation(association);
        }
        club = clubRepository.saveAndFlush(club);
        tp.setClub(club);
        //todo verband
        merge(tp, clickTTPlayer);
        return tp;
    }

    private boolean compareClub(TournamentPlayer dbP, Player clickTTPlayer) {
        if (dbP.getClub() == null) {
            return false;
        }
        return Objects.equals(clickTTPlayer.getPerson().get(0).getClubName(), dbP.getClub().getName());
    }

    private void merge(TournamentPlayer tp, Player clickTTPlayer) {
        Person person = clickTTPlayer.getPerson().get(0);
        tp.setQttr(Integer.valueOf(person.getTtr()));
        try {
            tp.setBirthday(DATE_FORMAT.parse(person.getBirthyear()));
        } catch (ParseException e) {
            //ignore
        }
        //todo fill
    }
}
