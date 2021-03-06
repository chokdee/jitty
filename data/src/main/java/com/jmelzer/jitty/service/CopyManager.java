/*
 * Copyright (c) 2018.
 * J. Melzer
 */

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 30.07.2016.
 * copy entities
 */
public class CopyManager {

    final static DateFormat hourFormatter = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);

    static public KOFieldDTO copyForBracket(KOField koField) {
        return copy(koField, true);
    }

    static public KOFieldDTO copy(KOField koField, boolean withSets) {
        KOFieldDTO dto = new KOFieldDTO();
        BeanUtils.copyProperties(koField, dto, "round");
        Round round = koField.getRound();
        RoundDTO lastRoundDto = null;
        do {
            RoundDTO rdto = new RoundDTO();
            BeanUtils.copyProperties(round, rdto, "games");
            rdto.setRoundType(copy(round.getRoundType()));
            if (lastRoundDto == null) {
                dto.setRound(rdto);
            } else {
                lastRoundDto.setNextRound(rdto);
            }
            lastRoundDto = rdto;
            for (TournamentSingleGame game : round.getGames()) {
                rdto.addGame(copy(game, withSets));
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

    static public TournamentSingleGameDTO copy(TournamentSingleGame game, boolean withSets) {
        TournamentSingleGameDTO dto = new TournamentSingleGameDTO();
        BeanUtils.copyProperties(game, dto, "player1", "player2", "sets", "round");
        if (game.getPlayer1() != null) {
            dto.setPlayer1(copy(game.getPlayer1()));
        }
        if (game.getPlayer2() != null) {
            dto.setPlayer2(copy(game.getPlayer2()));
        }
        if (game.getGroup() != null) {
            dto.setGroup(copy(game.getTcName(), game.getGroup()));
        }
        if (withSets && game.getSets() != null && !game.getSets().isEmpty()) {
            for (GameSet gameSet : game.getSets()) {
                dto.addSet(copy(gameSet));
            }
        }
        if (game.getRound() != null) {
            RoundDTO rdto = new RoundDTO();
            BeanUtils.copyProperties(game.getRound(), rdto, "games");
            rdto.setRoundType(copy(game.getRound().getRoundType()));
            dto.setRound(rdto);
        }
        return dto;

    }

    public static TournamentPlayerDTO copy(TournamentPlayer player) {
        TournamentPlayerDTO dto = new TournamentPlayerDTO();
        BeanUtils.copyProperties(player, dto, "playedGames", "games", "classes", "association", "classes", "tournaments");
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

    public static TournamentGroupDTO copy(String clzName, TournamentGroup group) {
        TournamentGroupDTO dto = new TournamentGroupDTO();
        BeanUtils.copyProperties(group, dto, "players", "tournamentClass");
        dto.setTournamentClass(new TournamentClassDTO());
        dto.getTournamentClass().setName(clzName);
        return dto;
    }

    public static GameSetDTO copy(GameSet set) {
        GameSetDTO dto = new GameSetDTO();
        BeanUtils.copyProperties(set, dto);
        return dto;
    }

    static public KOFieldDTO copy(KOField koField) {
        return copy(koField, false);
    }

    static public void copy(TournamentClassDTO source, TournamentClass target, TournamentPlayerRepository playerRepository) {
        BeanUtils.copyProperties(source, target, "players", "system");

//        for (TournamentGroupDTO group : source.getGroups()) {
//            target.addGroup(copy(group, playerRepository));
//        }
    }

    public static List<TournamentSingleGame> copy(List<TournamentSingleGameDTO> dtos, String name, Long tid) {
        List<TournamentSingleGame> games = new ArrayList<>();
        for (TournamentSingleGameDTO dto : dtos) {
            TournamentSingleGame game = new TournamentSingleGame();
            copy(dto, game);
            game.setTcName(name);
            game.setTid(tid);
            games.add(game);
        }
        return games;

    }
    public static void copy(TournamentSingleGameDTO dto, TournamentSingleGame game) {
        BeanUtils.copyProperties(dto, game, "group", "player1", "player2", "sets");
        game.getSets().clear();
        for (GameSetDTO gameSet : dto.getSets()) {
            game.getSets().add(copy(gameSet));
        }
    }

    public static GameSet copy(GameSetDTO dto) {
        GameSet gameSet = new GameSet();
        BeanUtils.copyProperties(dto, gameSet);
        return gameSet;
    }

    public static TournamentDTO copy(Tournament tournament) {
        TournamentDTO dto = new TournamentDTO();
        BeanUtils.copyProperties(tournament, dto, "classes");
        for (TournamentClass tournamentClass : tournament.getClasses()) {
            dto.addClass(copyOnly(tournamentClass));
        }
        return dto;
    }

    static public TournamentClassDTO copyOnly(TournamentClass clz) {
        TournamentClassDTO dto = new TournamentClassDTO();
        BeanUtils.copyProperties(clz, dto, "players", "system");
        return dto;
    }

    public static Tournament copy(TournamentDTO dto, Tournament t) {
        BeanUtils.copyProperties(dto, t, "classes");
        return t;
    }

    static public GroupPhaseDTO copy(GroupPhaseDTO source, GroupPhase target, TournamentPlayerRepository playerRepository) {
        BeanUtils.copyProperties(source, target, "groups", "system");
        if (source.getGroups() != null) {
            for (TournamentGroupDTO group : source.getGroups()) {
                target.addGroup(copy(group, playerRepository));
            }
        }
        return source;
    }

    public static TournamentGroup copy(TournamentGroupDTO dto, TournamentPlayerRepository playerRepository) {
        TournamentGroup group = new TournamentGroup();
        BeanUtils.copyProperties(dto, group, "players");
        for (TournamentPlayerDTO tournamentPlayerDTO : dto.getPlayers()) {
            TournamentPlayer player = playerRepository.getOne(tournamentPlayerDTO.getId());
            group.addPlayer(player);
        }
        return group;
    }

    static public TournamentClassDTO copy(TournamentClass clz, boolean withSystem) {
        TournamentClassDTO dto = new TournamentClassDTO();
        BeanUtils.copyProperties(clz, dto, "system", "players");
        if (withSystem && clz.getSystem() != null) {
            dto.setSystem(new TournamentSystemDTO());
            for (Phase phase : clz.getSystem().getPhases()) {
                dto.getSystem().addPhase(copy(phase, true));
            }
        }

        return dto;
    }

    public static PhaseDTO copy(Phase phase, boolean onlyPhase) {
        if (phase instanceof GroupPhase) {
            return copy((GroupPhase) phase, onlyPhase);
        } else if (phase instanceof KOPhase) {
            return copy((KOPhase) phase, onlyPhase);
        } else if (phase instanceof SwissSystemPhase) {
            return copy((SwissSystemPhase) phase, onlyPhase);
        } else {
            throw new IllegalArgumentException("unkown " + phase);
        }
    }

    public static GroupPhaseDTO copy(GroupPhase groupPhase, boolean onlyPhase) {
        GroupPhaseDTO dto = new GroupPhaseDTO();
        BeanUtils.copyProperties(groupPhase, dto, "system", "groups", "system");
        if (!onlyPhase) {
            for (TournamentGroup group : groupPhase.getGroups()) {
                TournamentGroupDTO dtoGroup = copy(groupPhase.getSystem().getTournamentClass().getName(), group);
                dto.addGroup(dtoGroup);
                for (TournamentPlayer player : group.getPlayers()) {
                    dtoGroup.addPlayer(copy(player));
                }
            }
        }

        return dto;
    }

    public static KOPhaseDTO copy(KOPhase phase, boolean onlyPhase) {
        KOPhaseDTO dto = new KOPhaseDTO();
        BeanUtils.copyProperties(phase, dto, "koField", "system");
        return dto;
    }

    public static SwissSystemPhaseDTO copy(SwissSystemPhase phase, boolean onlyPhase) {
        SwissSystemPhaseDTO dto = new SwissSystemPhaseDTO();
        BeanUtils.copyProperties(phase, dto, "group", "system");
        if (!onlyPhase) {
            TournamentGroupDTO dtoGroup = copy(phase.getSystem().getTournamentClass().getName(), phase.getGroup());
            dto.setGroup(dtoGroup);
            for (TournamentPlayer player : phase.getGroup().getPlayers()) {
                dtoGroup.addPlayer(copy(player));
            }
        }
        return dto;
    }

    public static void fillResults(TournamentGroup group, GroupResultDTO groupResultDTO) {
        int pos = 1;
        for (PlayerStatistic ps : group.getRanking()) {
            GroupResultEntryDTO entry = new GroupResultEntryDTO();
            entry.setPos(pos++);
            entry.setClub(ps.player.getClub() != null ? ps.player.getClub().getName() : "");
            entry.setPlayerName(ps.player.getFullName());
            entry.setGameStat(ps.win + ":" + ps.lose);
            entry.setSetStat(ps.setsWon + ":" + ps.setsLost);
            entry.setDetailResult(ps.detailResult);
            groupResultDTO.getEntries().add(entry);
        }
    }
}
