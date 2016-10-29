package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.*;
import org.springframework.beans.BeanUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Created by J. Melzer on 30.07.2016.
 * copy entities
 */
public class CopyManager {

    final static DateFormat hourFormatter = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);

    static public KOFieldDTO copy(KOField koField) {
        KOFieldDTO dto = new KOFieldDTO();
        BeanUtils.copyProperties(koField, dto, "round");
        Round round = koField.getRound();
        RoundDTO lastRoundDto = null;
        do {
            RoundDTO rdto = new RoundDTO();
            BeanUtils.copyProperties(round, rdto, "games");
            rdto.setRoundType(copy(round.getRoundType()));
            if ( lastRoundDto == null) {
                dto.setRound(rdto);
            } else {
                lastRoundDto.setNextRound(rdto);
            }
            lastRoundDto = rdto;
            for (TournamentSingleGame game : round.getGames()) {
                rdto.addGame(copy(game));
            }
            round = round.getNextRound();
        } while (round != null);
        return dto;
    }

    private static RoundTypeDTO copy(RoundType roundType) {
        RoundTypeDTO dto = new RoundTypeDTO();
        dto.setValue(roundType.getValue());
        dto.setName(roundType.getName());
        return dto;
    }

    static public TournamentSingleGameDTO copy(TournamentSingleGame game) {
        TournamentSingleGameDTO dto = new TournamentSingleGameDTO();
        BeanUtils.copyProperties(game, dto, "player1", "player2", "sets");
        dto.setPlayer1(copy(game.getPlayer1()));
        dto.setPlayer2(copy(game.getPlayer2()));
        if (game.getGroup() != null) {
            dto.setGroup(copy(game.getGroup()));
            dto.setTcName(game.getGroup().getTournamentClass().getName());
        }
        return dto;

    }

    static public void copy(TournamentClassDTO dto, TournamentClass clz, TournamentPlayerRepository playerRepository) {
        BeanUtils.copyProperties(dto, clz, "groups", "players");
        clz.getGroups().clear();
        for (TournamentGroupDTO group : dto.getGroups()) {
            clz.addGroup(copy(group, playerRepository));
        }
    }

    public static void copy(TournamentSingleGameDTO dto, TournamentSingleGame game) {
        BeanUtils.copyProperties(dto, game, "group", "player1", "player2", "sets");
        game.getSets().clear();
        for (GameSetDTO gameSet : dto.getSets()) {
            game.getSets().add(copy(gameSet));
        }
    }

    public static TournamentDTO copy(Tournament tournament) {
        TournamentDTO dto = new TournamentDTO();
        BeanUtils.copyProperties(tournament, dto, "classes");
        for (TournamentClass tournamentClass : tournament.getClasses()) {
            dto.addClass(copyOnly(tournamentClass));
        }
        return dto;
    }

    public static Tournament copy(TournamentDTO dto, Tournament t) {
        BeanUtils.copyProperties(dto, t, "classes");
        return t;
    }

    public static GameSet copy(GameSetDTO dto) {
        GameSet gameSet = new GameSet();
        BeanUtils.copyProperties(dto, gameSet);
        return gameSet;
    }

    public static TournamentGroup copy(TournamentGroupDTO dto, TournamentPlayerRepository playerRepository) {
        TournamentGroup group = new TournamentGroup();
        BeanUtils.copyProperties(dto, group, "players");
        for (TournamentPlayerDTO tournamentPlayerDTO : dto.getPlayers()) {
            TournamentPlayer player = playerRepository.findOne(tournamentPlayerDTO.getId());
            group.addPlayer(player);
        }
        return group;
    }

    static public TournamentClassDTO copyOnly(TournamentClass clz) {
        TournamentClassDTO dto = new TournamentClassDTO();
        BeanUtils.copyProperties(clz, dto, "groups", "players");
        return dto;
    }

    static public TournamentClassDTO copy(TournamentClass clz) {
        TournamentClassDTO dto = new TournamentClassDTO();
        BeanUtils.copyProperties(clz, dto, "groups", "players");
        for (TournamentGroup group : clz.getGroups()) {
            TournamentGroupDTO dtoGroup = copy(group);
            dto.addGroup(dtoGroup);
            for (TournamentPlayer player : group.getPlayers()) {
                dtoGroup.addPlayer(copy(player));
            }
        }

        return dto;
    }

    public static TournamentPlayerDTO copy(TournamentPlayer player) {
        TournamentPlayerDTO dto = new TournamentPlayerDTO();
        BeanUtils.copyProperties(player, dto);
        if (player.getLastGameAt() != null) {
            LocalDateTime time = LocalDateTime.ofInstant(player.getLastGameAt().toInstant(), ZoneId.systemDefault());
            long h = ChronoUnit.HOURS.between(time, LocalDateTime.now());
            long m = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
            dto.setPeriodSinceLastGame(h + "h" + m + "m");
            dto.setLastGameAt("letztes Spiel um " + hourFormatter.format(player.getLastGameAt()));
        } else {
            dto.setPeriodSinceLastGame("--");
            dto.setLastGameAt("noch nicht gespielt");
        }
        return dto;
    }

    private static TournamentGroupDTO copy(TournamentGroup group) {
        TournamentGroupDTO dto = new TournamentGroupDTO();
        BeanUtils.copyProperties(group, dto, "players", "tournamentClass");
        dto.setTournamentClass(new TournamentClassDTO());
        dto.getTournamentClass().setName(group.getTournamentClass().getName());
        return dto;
    }
}
