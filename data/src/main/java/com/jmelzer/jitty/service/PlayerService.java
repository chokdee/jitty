package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.ClubRepository;
import com.jmelzer.jitty.dao.TournamentClassRepository;
import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.model.Club;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentPlayer;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.model.xml.playerimport.Competition;
import com.jmelzer.jitty.model.xml.playerimport.Person;
import com.jmelzer.jitty.model.xml.playerimport.Player;
import com.jmelzer.jitty.model.xml.playerimport.Tournament;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    TournamentClassRepository classRepository;

    @Transactional(readOnly = true)
    public List<TournamentPlayerDTO> findAll() {
        List<TournamentPlayer> players = repository.findAll();
        List<TournamentPlayerDTO> dtos = new ArrayList<>();
        for (TournamentPlayer player : players) {
            TournamentPlayerDTO dto = new TournamentPlayerDTO();
            BeanUtils.copyProperties(player, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Transactional
    public TournamentPlayerDTO findOne(Long aLong) {

        TournamentPlayer tournamentPlayer = repository.findOne(aLong);
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
    public void delete(Long aLong) {
        repository.delete(aLong);
    }

    @Transactional
    public void save(TournamentPlayerDTO player) {
        TournamentPlayer playerDB = null;

        if (player.getId() == null) {
            playerDB = new TournamentPlayer();
        } else {
            playerDB = repository.findOne(player.getId());
        }
        BeanUtils.copyProperties(player, playerDB, "classes");
        for (TournamentClassDTO classDTO : player.getClasses()) {
            playerDB.removeAllClasses();
            playerDB.addClass(classRepository.findOne(classDTO.getId()));
        }
        repository.save(playerDB);
    }

    @Transactional
    public int importPlayerFromClickTT(InputStream istream) {
        Tournament clickTTTournament = xmlImporter.parseClickTTPlayerExport(istream);
        int count = 0;
        //todo validate tournament
        for (Competition competition : clickTTTournament.getCompetition()) {
            for (Player clickTTPlayer : competition.getPlayers().getPlayer()) {

                List<TournamentPlayer> dbPlayers = repository.findByLastNameAndFirstName(clickTTPlayer.getPerson().get(0).getLastname(),
                        clickTTPlayer.getPerson().get(0).getFirstname());

                if (dbPlayers.size() == 0 || dbPlayers.size() > 1) {
                    TournamentPlayer dbP = createDbPlayer(clickTTPlayer);
                    count++;
                    repository.saveAndFlush(dbP);
                } else if (dbPlayers.size() == 1) {
                    TournamentPlayer dbP = dbPlayers.get(0);
                    if (compareClub(dbP, clickTTPlayer)) {
                        merge(dbP, clickTTPlayer);
                    } else {
                        dbP = createDbPlayer(clickTTPlayer);
                    }
                    count++;
                    repository.saveAndFlush(dbP);
                }
            }
        }

        return count;
    }

    private TournamentPlayer createDbPlayer(Player clickTTPlayer) {
        Person person = clickTTPlayer.getPerson().get(0);
        TournamentPlayer tp = new TournamentPlayer();
        tp.setFirstName(person.getFirstname());
        tp.setLastName(person.getLastname());
        tp.setGender(person.getSex().equals("0") ? "w" : "m");
        Club club = clubRepository.findByName(person.getClubName());
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
