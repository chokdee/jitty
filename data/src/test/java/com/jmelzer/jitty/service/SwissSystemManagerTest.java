/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.dto.GameSetDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import com.jmelzer.jitty.model.xml.playerimport.Match;
import com.jmelzer.jitty.model.xml.playerimport.Person;
import com.jmelzer.jitty.model.xml.playerimport.Player;
import com.jmelzer.jitty.model.xml.playerimport.Tournament;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 17.03.2017.
 */
public class SwissSystemManagerTest {
    SwissSystemManager swissSystemManager = new SwissSystemManager();

    @Test
    public void calcRankingFirstRound() throws Exception {
        List<TournamentPlayerDTO> player = createPlayer();
        swissSystemManager.calcRankingFirstRound(player);
        assertEquals(1500, player.get(0).getQttr());
        assertEquals(1200, player.get(1).getQttr());
        assertEquals(1100, player.get(2).getQttr());
        assertEquals(1000, player.get(3).getQttr());
    }

    private List<TournamentPlayerDTO> createPlayer() {
        List<TournamentPlayerDTO> player = new ArrayList<>();
        player.add(new TournamentPlayerDTO("1", 1000));
        player.add(new TournamentPlayerDTO("2", 1200));
        player.add(new TournamentPlayerDTO("3", 1100));
        player.add(new TournamentPlayerDTO("4", 1500));
        player.add(new TournamentPlayerDTO("5", 999));
        player.add(new TournamentPlayerDTO("6", 998));
        player.add(new TournamentPlayerDTO("7", 997));
        player.add(new TournamentPlayerDTO("8", 996));
        player.add(new TournamentPlayerDTO("9", 995));
        player.add(new TournamentPlayerDTO("10", 994));
        player.add(new TournamentPlayerDTO("11", 993));
        player.add(new TournamentPlayerDTO("12", 992));
        return player;
    }

    @Test
    public void calcRankingSixRounds() throws Exception {
        for (int i = 0; i < 1000; i++) {

            System.out.println("HHHHHHHHHHHHHH   " + i + " HHHHHHHHHHHHHH");
            List<TournamentPlayerDTO> player = createPlayer();
            swissSystemManager.calcRankingFirstRound(player);
            System.out.println("------ round 1 starts .-------------- ");
            List<TournamentSingleGameDTO> games = swissSystemManager.createGamesForTheFirstRound(player);

            for (TournamentSingleGameDTO game : games) {
                System.out.println("game = " + game);
                game.setWinner(2);
            }
            for (int round = 2; round <= 6; round++) {

                System.out.println("------ round " + round + " starts .-------------- ");
                swissSystemManager.calcRankingRound(round, player);

                for (TournamentPlayerDTO playerDTO : player) {
                    System.out.println("playerDTO = " + playerDTO);
                }

                games = createGames(player, round);
                for (TournamentSingleGameDTO game : games) {
                    System.out.println("game = " + game);
                    game.setWinner(1);
                }
            }

            System.out.println(" ### RESULT ###");
            swissSystemManager.calcRankingRound(7, player);
            for (TournamentPlayerDTO playerDTO : player) {
                System.out.println("playerDTO = " + playerDTO);
            }
        }
    }

    private List<TournamentSingleGameDTO> createGames(List<TournamentPlayerDTO> player, int round) {
        List<TournamentSingleGameDTO> games;
        games = null;
        for (int i = 1; i <= 100; i++) {
            try {
                System.out.println("------ #" + i + " run -------------- ");
                if (i > 80) {
                    System.out.println("brute force !!");
                    games = swissSystemManager.createGamesForRound(round, player, true);
                } else {
                    games = swissSystemManager.createGamesForRound(round, player, false);
                }
                break;
            } catch (SwissRuntimeException e) {
                System.out.println();
            }
        }
        return games;
    }

    /**
     * see  https://wttv.click-tt.de/cgi-bin/WebObjects/nuLigaTTDE.woa/wa/tournamentMatchesReport?circuit=2017_Turnierserie&federation=WTTV&date=2017-03-01&competition=350743
     *
     * @throws Exception
     */
    @Test
    @Ignore("only for show error in mktt")
    public void importPlayerFromClickTT() throws Exception {
        XMLImporter xmlImporter = new XMLImporter();
        InputStream inputStream = getClass().getResourceAsStream("/xml-import/androWTTV-Cup.xml");

        com.jmelzer.jitty.model.xml.playerimport.Tournament tournament = xmlImporter.parseClickTTPlayerExport(inputStream);
        List<TournamentPlayerDTO> player = new ArrayList<>();


        for (Player xmlPlayer : tournament.getCompetition().get(0).getPlayers().getPlayer()) {
            Person person = xmlPlayer.getPerson().get(0);
            player.add(new TournamentPlayerDTO(xmlPlayer.getId(), person.getFirstname() + " " + person.getLastname(), Integer.valueOf(person.getTtr())));
        }

        swissSystemManager.calcRankingFirstRound(player);

        for (TournamentPlayerDTO tournamentPlayerDTO : player) {
            System.out.println("tournamentPlayerDTO = " + tournamentPlayerDTO);

        }
        assertEquals("Marcus Meuser", player.get(0).getFullName());
        assertEquals("J\u00FCrgen Jakob", player.get(1).getFullName());
        //todo ...

        int round = 1;
        int matchNr = 0;
        matchNr = runRoundFromFile(tournament, player, round, matchNr);

        round++;
        matchNr = runRoundFromFile(tournament, player, round, matchNr);
        round++;
        matchNr = runRoundFromFile(tournament, player, round, matchNr);
        round++;
        matchNr = runRoundFromFile(tournament, player, round, matchNr);
        round++;
        matchNr = runRoundFromFile(tournament, player, round, matchNr);
        System.out.println("------ round " + round + " starts .-------------- ");
        List<TournamentSingleGameDTO> games = createGames(player, round);
//        List<TournamentSingleGameDTO> games = swissSystemManager.createGamesForRound(round, player, false);
        for (TournamentSingleGameDTO game : games) {
            System.out.println( game.getPlayer1().getFullName() + " --> " + game.getPlayer2().getFullName());
        }
    }

    private int runRoundFromFile(Tournament tournament, List<TournamentPlayerDTO> player, int round, int matchNr) {
        int matchNr1 = matchNr;
        List<TournamentSingleGameDTO> games;
        System.out.println("------ round " + round + " starts .-------------- ");
        System.out.println("------------------------------------------------------------------------------------------------------");
        games = fillGamesFrom(round, tournament, player);

        for (TournamentSingleGameDTO game : games) {
            Match match = tournament.getCompetition().get(0).getMatches().getMatch().get(matchNr1);
            matchNr1++;
            game.setSets(convertToSets(match));
            new TournamentService().calcWinner(game);
            assertTrue(match.getGroup().contains("Runde  " + round));
            System.out.println("game = " + game);
        }
        swissSystemManager.calcRankingRound(round, player);
        return matchNr1;
    }

    private List<TournamentSingleGameDTO> fillGamesFrom(int round, Tournament tournament, List<TournamentPlayerDTO> player) {
        List<TournamentSingleGameDTO> games = new ArrayList<>();
        for (Match match : tournament.getCompetition().get(0).getMatches().getMatch()){
            if (!match.getGroup().contains("Runde  " + round)) {
                continue;
            }
            games.add(createGame(match, player));

        }
        return games;
    }

    private TournamentSingleGameDTO createGame(Match match, List<TournamentPlayerDTO> player) {
        TournamentSingleGameDTO game = new TournamentSingleGameDTO();
        game.setPlayer1AndBackReference(findPlayer((Player) match.getPlayerA(), player));
        game.setPlayer2AndBackReference(findPlayer((Player) match.getPlayerB(), player));
        return game;
    }

    private TournamentPlayerDTO findPlayer(Player playerA, List<TournamentPlayerDTO> player) {
        for (TournamentPlayerDTO tournamentPlayerDTO : player) {
            if (playerA.getId().equals(tournamentPlayerDTO.getImportId()))
                return tournamentPlayerDTO;
        }
        throw new RuntimeException("not found " + playerA.getId());
    }

    private List<GameSetDTO> convertToSets(Match match) {
        List<GameSetDTO> sets = new ArrayList<>();
        sets.add(new GameSetDTO(Integer.valueOf(match.getSetA1()), Integer.valueOf(match.getSetB1())));
        sets.add(new GameSetDTO(Integer.valueOf(match.getSetA2()), Integer.valueOf(match.getSetB2())));
        sets.add(new GameSetDTO(Integer.valueOf(match.getSetA3()), Integer.valueOf(match.getSetB3())));

        if (!match.getSetA4().isEmpty()) {
            sets.add(new GameSetDTO(Integer.valueOf(match.getSetA4()), Integer.valueOf(match.getSetB4())));
        }
        if (!match.getSetA5().isEmpty()) {
            sets.add(new GameSetDTO(Integer.valueOf(match.getSetA5()), Integer.valueOf(match.getSetB5())));
        }
        return sets;
    }
}