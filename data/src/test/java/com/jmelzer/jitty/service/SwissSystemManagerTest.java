/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentClassRepository;
import com.jmelzer.jitty.exceptions.IntegrityViolation;
import com.jmelzer.jitty.model.SwissSystemPhase;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentSingleGame;
import com.jmelzer.jitty.model.TournamentSystemType;
import com.jmelzer.jitty.model.dto.GameSetDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import com.jmelzer.jitty.model.xml.clicktt.Match;
import com.jmelzer.jitty.model.xml.clicktt.Person;
import com.jmelzer.jitty.model.xml.clicktt.Player;
import com.jmelzer.jitty.model.xml.clicktt.Tournament;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.jmelzer.jitty.model.PhaseCombination.SWS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by J. Melzer on 17.03.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class SwissSystemManagerTest {
    @InjectMocks
    SwissSystemManager swissSystemManager = new SwissSystemManager();

    @Mock
    TournamentClassRepository tcRepository;

    @Mock
    WorkflowManager workflowManager;

    @Test
    public void calcRankingFirstRound() throws Exception {
        List<TournamentPlayerDTO> player = createPlayer(12);
        swissSystemManager.calcRankingFirstRound(player);
        assertEquals(1500, player.get(0).getQttr());
        assertEquals(1200, player.get(1).getQttr());
        assertEquals(1100, player.get(2).getQttr());
        assertEquals(1000, player.get(3).getQttr());
    }

    private List<TournamentPlayerDTO> createPlayer(int count) {
        List<TournamentPlayerDTO> player = new ArrayList<>();
        player.add(new TournamentPlayerDTO(1L, "1", 1000));
        player.add(new TournamentPlayerDTO(2L, "2", 1200));
        player.add(new TournamentPlayerDTO(3L, "3", 1100));
        player.add(new TournamentPlayerDTO(4L, "4", 1500));
        if (count > 4) {
            for (int i = 5; i <= count; i++) {
                player.add(new TournamentPlayerDTO((long) i, "" + i, 999 - i));

            }
        }
//        player.add(new TournamentPlayerDTO("6", 998));
//        player.add(new TournamentPlayerDTO("7", 997));
//        player.add(new TournamentPlayerDTO("8", 996));
//        player.add(new TournamentPlayerDTO("9", 995));
//        player.add(new TournamentPlayerDTO("10", 994));
//        player.add(new TournamentPlayerDTO("11", 993));
//        player.add(new TournamentPlayerDTO("12", 992));
        return player;
    }

    @Test
    public void calcRankingSixRounds() throws Exception {
        for (int i = 0; i < 1; i++) {

//            System.out.println("HHHHHHHHHHHHHH   " + i + " HHHHHHHHHHHHHH");
            List<TournamentPlayerDTO> player = createPlayer(11);
            swissSystemManager.calcRankingFirstRound(player);
//            System.out.println("------ round 1 starts .-------------- ");
            List<TournamentSingleGameDTO> games = swissSystemManager.createGamesForTheFirstRound(player).getGames();

            for (TournamentSingleGameDTO game : games) {
                game.setWinner(2);
            }
            swissSystemManager.calcRankingRound(TournamentSystemType.AC, 1, player);
            //the last one do not have a played game
            for (int j = 0; j < player.size() - 1; j++) {
                TournamentPlayerDTO playerDTO = player.get(j);
//                System.out.println("playerDTO = " + playerDTO);
                assertEquals("#" + j + playerDTO.toString(), 1, playerDTO.getPlayedGames().size());
            }
            assertTrue(player.get(player.size() - 1).hasFreilos());

            for (int round = 2; round <= 6; round++) {

//                System.out.println("------ round " + round + " starts .-------------- ");
                swissSystemManager.calcRankingRound(TournamentSystemType.AC, round, player);

//                for (TournamentPlayerDTO playerDTO : player) {
//                    System.out.println("playerDTO = " + playerDTO);
//                }

                games = createGames(player, round);
                for (TournamentSingleGameDTO game : games) {
//                    System.out.println("game = " + game);
                    game.setWinner(1);
                }
            }

//            System.out.println(" ### RESULT ###");
            swissSystemManager.calcRankingRound(TournamentSystemType.AC, 7, player);
//            for (TournamentPlayerDTO playerDTO : player) {
//                System.out.println("playerDTO = " + playerDTO);
//            }
        }
    }

    private List<TournamentSingleGameDTO> createGames(List<TournamentPlayerDTO> player, int round) {
        List<TournamentSingleGameDTO> games;
        games = null;
        for (int i = 1; i <= 100; i++) {
            try {
//                System.out.println("------ #" + i + " run -------------- ");
                if (i > 80) {
//                    System.out.println("brute force !!");
                    games = swissSystemManager.createGamesForRound(round, player, true).getGames();
                } else {
                    games = swissSystemManager.createGamesForRound(round, player, false).getGames();
                }
                break;
            } catch (SwissRuntimeException e) {
//                System.out.println();
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

        com.jmelzer.jitty.model.xml.clicktt.Tournament tournament = xmlImporter.parseClickTTPlayerExport(inputStream);
        List<TournamentPlayerDTO> player = new ArrayList<>();


        for (Player xmlPlayer : tournament.getCompetition().get(0).getPlayers().getPlayer()) {
            Person person = xmlPlayer.getPerson().get(0);
            player.add(new TournamentPlayerDTO(xmlPlayer.getId(), person.getFirstname() + " " + person.getLastname(), Integer.valueOf(person.getTtr())));
        }

        swissSystemManager.calcRankingFirstRound(player);

//        for (TournamentPlayerDTO tournamentPlayerDTO : player) {
//            System.out.println("tournamentPlayerDTO = " + tournamentPlayerDTO);
//
//        }
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
//        System.out.println("------ round " + round + " starts .-------------- ");
        List<TournamentSingleGameDTO> games = createGames(player, round);
//        List<TournamentSingleGameDTO> games = swissSystemManager.createGamesForRound(round, player, false);
//        for (TournamentSingleGameDTO game : games) {
//            System.out.println(game.getPlayer1().getFullName() + " --> " + game.getPlayer2().getFullName());
//        }
    }

    private int runRoundFromFile(Tournament tournament, List<TournamentPlayerDTO> player, int round, int matchNr) {
        int matchNr1 = matchNr;
        List<TournamentSingleGameDTO> games;
//        System.out.println("------ round " + round + " starts .-------------- ");
//        System.out.println("------------------------------------------------------------------------------------------------------");
        games = fillGamesFrom(round, tournament, player);

        for (TournamentSingleGameDTO game : games) {
            Match match = tournament.getCompetition().get(0).getMatches().getMatch().get(matchNr1);
            matchNr1++;
            game.setSets(convertToSets(match));
            new TournamentService().calcWinner(game);
            assertTrue(match.getGroup().contains("Runde  " + round));
//            System.out.println("game = " + game);
        }
        swissSystemManager.calcRankingRound(TournamentSystemType.AC, round, player);
        return matchNr1;
    }

    private List<TournamentSingleGameDTO> fillGamesFrom(int round, Tournament tournament, List<TournamentPlayerDTO> player) {
        List<TournamentSingleGameDTO> games = new ArrayList<>();
        for (Match match : tournament.getCompetition().get(0).getMatches().getMatch()) {
            if (!match.getGroup().contains("Runde  " + round)) {
                continue;
            }
            games.add(createGame(match, player));

        }
        return games;
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

    private TournamentSingleGameDTO createGame(Match match, List<TournamentPlayerDTO> player) {
        TournamentSingleGameDTO game = new TournamentSingleGameDTO();
        game.setPlayer1AndBackReference(findPlayer((Player) match.getPlayerA(), player));
        game.setPlayer2AndBackReference(findPlayer((Player) match.getPlayerB(), player));
        return game;
    }

    private TournamentPlayerDTO findPlayer(Player playerA, List<TournamentPlayerDTO> player) {
        for (TournamentPlayerDTO tournamentPlayerDTO : player) {
            if (playerA.getId().equals(tournamentPlayerDTO.getImportId())) {
                return tournamentPlayerDTO;
            }
        }
        throw new RuntimeException("not found " + playerA.getId());
    }

    @Test
    public void createtNextSwissRoundIfNecessary() throws Exception {
        TournamentClass tc = new TournamentClass();
        tc.createPhaseCombination(SWS);
        tc.setActivePhaseNo(0);
        SwissSystemPhase activePhase = (SwissSystemPhase) tc.getActivePhase();
        TournamentSingleGame game = new TournamentSingleGame();
        activePhase.getGroup().addGame(game);

        when(tcRepository.getOne(1L)).thenReturn(tc);
        assertEquals("games are not finished", 1, swissSystemManager.createtNextSwissRoundIfNecessary(1L, 1));

        game.setWinner(1);


        assertEquals(1, swissSystemManager.createtNextSwissRoundIfNecessary(1L, 1));
        assertThat(tc.getPhaseCount(), is(2));
        assertTrue(swissSystemManager.activatePhase(1L, 2));

        //called it twice, shall be the same result
        assertEquals(2, swissSystemManager.createtNextSwissRoundIfNecessary(1L, 1));
        assertThat(tc.getPhaseCount(), is(2));

        for (int i = 2; i <= 6; i++) {

            activePhase = (SwissSystemPhase) tc.getActivePhase();
            game = new TournamentSingleGame();
            activePhase.getGroup().addGame(game);
            try {
                assertEquals("games are not finished", i, swissSystemManager.createtNextSwissRoundIfNecessary(1L, i));
            } catch (IntegrityViolation integrityViolation) {
                if (i != 6) {
                    fail();
                }
                break;
            }

            game.setWinner(1);

            try {
                //round 6 is special
                assertEquals(i, swissSystemManager.createtNextSwissRoundIfNecessary(1L, i));
                assertThat(tc.getPhaseCount(), is(i + 1));
                assertTrue(swissSystemManager.activatePhase(1L, i + 1));
            } catch (IntegrityViolation integrityViolation) {
                if (i != 6) {
                    fail();
                }
                assertThat(tc.getPhaseCount(), is(i));
            }
        }

    }

    @Test
    public void isOdd() throws Exception {
        assertTrue(swissSystemManager.isOdd(1));
        assertTrue(swissSystemManager.isOdd(3));
        assertFalse(swissSystemManager.isOdd(2));
        assertFalse(swissSystemManager.isOdd(4));
    }


}