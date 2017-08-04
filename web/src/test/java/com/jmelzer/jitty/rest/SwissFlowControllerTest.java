/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.AgeGroup;
import com.jmelzer.jitty.model.TournamentSystemType;
import com.jmelzer.jitty.model.dto.*;
import com.jmelzer.jitty.utl.RandomUtil;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

import static com.jmelzer.jitty.rest.TestUtil.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 26.07.2016.
 * Test draw controller
 */
@SuppressWarnings("OverlyComplexMethod")
public class SwissFlowControllerTest extends SecureResourceTest {

    public static final String TC_NAME = "swiss-flow";

    static int TABLE_COUNT = 6;


    @Test
    public void flow() throws Exception {
        try {
            doLogin();

            ResponseEntity<TournamentSingleGameDTO[]> possibleGamesEntity = null;
            ResponseEntity<SwissDraw> swissDrawResponseEntity = null;
            ResponseEntity<SwissSystemPhaseDTO> phaseEntity = null;
            ResponseEntity<TournamentPlayerDTO[]> psEntity = null;
            int possibleGamesBeforeStartTest = 0;
//            assertEquals(possibleGamesBeforeStartTest, possibleGamesEntity.getBody().length);
            ResponseEntity<TournamentSingleGameDTO[]> runningGamesEntity = null;
            int runningGamesBeforeStartTest = 0;
            //Turnier laeuft, es gibt in der db aber bereits ein paar moegliche und beendete Spiele, die zaehlen wir hier
            int finishedGamesBeforeStartTest = 0;//http(HttpMethod.GET, "api/tournamentdirector/finished-games", createHttpEntity(null, loginHeaders), TournamentSingleGameDTO[].class).getBody().length;

            Long tId = createTournament();

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);


            //create a class
            TournamentClassDTO tournamentClass = createClz(tId);
            long tClassId = tournamentClass.getId();

            final int PLAYERSIZE = 11;
            for (int i = 0; i < PLAYERSIZE; i++) {
                createPlayer(i, tournamentClass);
            }
            final int MAX_PARALLEL_GAMES_COUNT = PLAYERSIZE / 2;

            //start the class
            ResponseEntity<Void> voidEntity = http(HttpMethod.GET, "api/draw/start-swiss-class?cid=" + tClassId, createHttpEntity(null, loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            //query the round
            ResponseEntity<Integer> intE = http(HttpMethod.GET, "api/draw/swiss-round?cid=" + tClassId,
                    createHttpEntity(null, loginHeaders), Integer.class);
            assertThat(intE.getBody(), is(1));

            int possGames = 0;
            //auslosung  speichern und starten
            for (int round = 1; round <= 6; round++) {
                System.out.println("####################    start round " + (round) + " ####################");


                intE = http(HttpMethod.GET, "api/draw/create-next-swiss-round-if-necessary?cid=" + tClassId + "&round=" + round,
                        createHttpEntity(null, loginHeaders), Integer.class);
                assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
                assertEquals("round isn't active", round == 1 ? 1 : round - 1, (int) intE.getBody());


                //auslosung
                swissDrawResponseEntity = http(HttpMethod.GET, "api/draw/calc-swiss-draw?cid=" + tClassId + "&round=" + round,
                        createHttpEntity(null, loginHeaders), SwissDraw.class);
                assertTrue(swissDrawResponseEntity.getStatusCode().is2xxSuccessful());
                assertThat(swissDrawResponseEntity.getBody().getGames().size(), is(PLAYERSIZE / 2));
                if (round > 1) {
                    validateStatus(tClassId, TournamentClassStatus.SWISS_PHASE_FINISHED); //transient not saved
                } else {
                    validateStatus(tClassId, TournamentClassStatus.NOTSTARTED); //transient not saved
                }

                voidEntity = http(HttpMethod.POST, "api/draw/save-swiss-round?cid=" + tClassId + "&round=" + round,
                        createHttpEntity(swissDrawResponseEntity.getBody().getGames(), loginHeaders), Void.class);
                assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
                validateStatus(tClassId, TournamentClassStatus.SWISS_PHASE_DRAW_BUT_NOT_STARTED);

//                save it twice shall no have any effect
                voidEntity = http(HttpMethod.POST, "api/draw/save-swiss-round?cid=" + tClassId + "&round=" + round,
                        createHttpEntity(swissDrawResponseEntity.getBody().getGames(), loginHeaders), Void.class);
                assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

                swissDrawResponseEntity = http(HttpMethod.GET, "api/draw/get-swiss-draw?cid=" + tClassId + "&round=" + round,
                        createHttpEntity(null, loginHeaders), SwissDraw.class);
                assertTrue(swissDrawResponseEntity.getStatusCode().is2xxSuccessful());
                assertThat(swissDrawResponseEntity.getBody().getGames().size(), is(PLAYERSIZE / 2));

                //start the round
                voidEntity = http(HttpMethod.GET, "api/draw/start-swiss-round?cid=" + tClassId + "&round=" + round,
                        createHttpEntity(null, loginHeaders), Void.class);
                assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
                validateStatus(tClassId, TournamentClassStatus.SWISS_PHASE_DRAW_BUT_NOT_STARTED);

                phaseEntity = http(HttpMethod.GET, "api/draw/actual-phase?cid=" + tClassId,
                        createHttpEntity(null, loginHeaders), SwissSystemPhaseDTO.class);
                assertTrue(phaseEntity.getStatusCode().is2xxSuccessful());
                assertThat(phaseEntity.getBody().getRound(), is(round));

                //query the round
                intE = http(HttpMethod.GET, "api/draw/swiss-round?cid=" + tClassId,
                        createHttpEntity(null, loginHeaders), Integer.class);
                assertThat(intE.getBody(), is(round));

                assertThat(jdbcTemplate.queryForObject("select count(*) from tournament_group where id = " + phaseEntity.getBody().getGroup().getId(), Integer.class), is(1));

                possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                        createHttpEntity(phaseEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                assertThat(possibleGamesEntity.getBody().length, is(MAX_PARALLEL_GAMES_COUNT));
                possGames += MAX_PARALLEL_GAMES_COUNT;
                TournamentSingleGameDTO[] possibleGames = possibleGamesEntity.getBody();
                assertTrue(possibleGamesEntity.getStatusCode().is2xxSuccessful());

                //start the games we have found but max TABLE_COUNT
                ResponseEntity<Void> ve = http(HttpMethod.GET, "api/tournamentdirector/start-possible-games", createHttpEntity(null, loginHeaders), Void.class);
                assertTrue(ve.getStatusCode().is2xxSuccessful());

                //check the "running games" functionality
                runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                        createHttpEntity(phaseEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                assertTrue(runningGamesEntity.getStatusCode().is2xxSuccessful());
                assertThat(runningGamesEntity.getBody().length, is(MAX_PARALLEL_GAMES_COUNT));

                //no more possible games, all are running
                possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                        createHttpEntity(phaseEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                //6 Tische und 6 laufen, d.h. 0 noch möglich
                assertThat(possibleGamesEntity.getBody().length, is(0));

                //enter results for all games
                TournamentSingleGameDTO[] runningGames = runningGamesEntity.getBody();
                addAndSaveResult(loginHeaders, tClassId, runningGames);
                if (round < 6) {
                    validateStatus(tClassId, TournamentClassStatus.SWISS_PHASE_FINISHED);
                } else {
                    validateStatus(tClassId, TournamentClassStatus.FINISHED);
                }

                //finished games must now the count of the entered results
                ResponseEntity<TournamentSingleGameDTO[]> finishedGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/finished-games",
                        createHttpEntity(phaseEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                assertThat(finishedGamesEntity.getBody().length, is(possGames));


            }
            validateStatus(tClassId, TournamentClassStatus.FINISHED);

            assertThat(possGames, is(6 * MAX_PARALLEL_GAMES_COUNT));

            System.out.println("all rounds completed, yeah");
            psEntity = http(HttpMethod.GET, "api/draw/possible-player-swiss-system?cid=" + tClassId,
                    createHttpEntity(null, loginHeaders), TournamentPlayerDTO[].class);
            int i = 0;
            for (TournamentPlayerDTO tournamentPlayerDTO : psEntity.getBody()) {
                System.out.println("#" + ++i + ": " + tournamentPlayerDTO.getFirstName() + " - " + tournamentPlayerDTO.getWonGames() +
                        " - " + tournamentPlayerDTO.getBuchholzZahl() + " - " +
                        tournamentPlayerDTO.getFeinBuchholzZahl());
            }


        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    private Long createTournament() {
        //create a tournament
        TournamentDTO tournament = new TournamentDTO();
        tournament.setName("Flowtest");
        tournament.setTableCount(TABLE_COUNT);
        tournament.setStartDate(new Date());
        tournament.setEndDate(new Date());
        ResponseEntity<Long> longEntitiy = http(HttpMethod.POST, "api/tournaments",
                createHttpEntity(tournament, loginHeaders), Long.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
        Long tId = longEntitiy.getBody();

        http(HttpMethod.GET, "api/tournaments/actual/" + tId, createHttpEntity(tournament, loginHeaders), Void.class);
        return tId;
    }

    private TournamentClassDTO createClz(Long tId) {
        TournamentClassDTO tournamentClass = new TournamentClassDTO();
        tournamentClass.setName(TC_NAME);
        tournamentClass.setStartTTR(0);
        tournamentClass.setEndTTR(3000);
        tournamentClass.setAgeGroup(AgeGroup.DH.getValue());
        tournamentClass.setSystemType(TournamentSystemType.AC.getValue());

        ResponseEntity<Long> longEntitiy = http(HttpMethod.POST, "api/tournament-classes/" + tId,
                createHttpEntity(tournamentClass, loginHeaders), Long.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
        Long tClassId = longEntitiy.getBody();
        assertNotNull(tClassId);

        ResponseEntity<GroupPhaseDTO> phaseDTO = http(HttpMethod.GET, "api/draw/actual-phase?cid=" + tClassId,
                createHttpEntity(null, loginHeaders), GroupPhaseDTO.class);

        assertNull("still not drawed", phaseDTO.getBody());
        //return fresh copy
        return http(HttpMethod.GET, "api/tournament-classes/" + tClassId,
                createHttpEntity(null, loginHeaders), TournamentClassDTO.class).getBody();
    }

    private void createPlayer(int i, TournamentClassDTO tournamentClass) {
        TournamentPlayerDTO player = new TournamentPlayerDTO();
        player.setFirstName(i + " aaaa");
        player.setLastName(i + " bbbb");
        player.setQttr(1660 + i);
        player.addClass(tournamentClass);
        ResponseEntity<Void> longEntitiy = http(HttpMethod.POST, "api/players",
                createHttpEntity(player, loginHeaders), Void.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
    }

    private void validateStatus(long tClassId, TournamentClassStatus status) {
        ResponseEntity<TournamentClassDTO> tcEntity = http(HttpMethod.GET, "api/tournament-classes/" + tClassId,
                createHttpEntity(null, loginHeaders), TournamentClassDTO.class);
        assertTrue(tcEntity.getStatusCode().is2xxSuccessful());
        assertThat(tcEntity.getBody().getStatus(), is(status));
    }

    private int addAndSaveResult(HttpHeaders loginHeaders, Long cid, TournamentSingleGameDTO[] runningGames) {
        ResponseEntity<Long> longResponseEntity;
        String sql = "select ID, POINTS1, POINTS2  from TOURNAMENT_SINGLE_GAME_SET gs, GAME_SET g where gs.SETS_ID = g.id and gs.TOURNAMENT_SINGLE_GAME_ID =  ";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        for (TournamentSingleGameDTO runningGame : runningGames) {
            int n = RandomUtil.randomIntFromInterval(0, 1);
            if (n == 0) {
                runningGame.addSet(new GameSetDTO(11, 2));
                runningGame.addSet(new GameSetDTO(11, 2));
                runningGame.addSet(new GameSetDTO(11, 2));
            } else if (n == 1) {
                runningGame.addSet(new GameSetDTO(9, 11));
                runningGame.addSet(new GameSetDTO(9, 11));
                runningGame.addSet(new GameSetDTO(11, 2));
                runningGame.addSet(new GameSetDTO(11, 13));
            } else {
                throw new RuntimeException("unknown " + n);
            }

            longResponseEntity = http(HttpMethod.POST, "api/tournamentdirector/save-result",
                    createHttpEntity(runningGame, loginHeaders), Long.class);
            assertTrue(longResponseEntity.getStatusCode().is2xxSuccessful());

        }
        return runningGames.length;
    }

    private TournamentSingleGameDTO startGame(Long gameId) {
        ResponseEntity<TournamentSingleGameDTO> re = http(HttpMethod.GET, "api/tournamentdirector/start-game?id=" + gameId,
                createHttpEntity(null, loginHeaders), TournamentSingleGameDTO.class);
        assertTrue(re.getStatusCode().is2xxSuccessful());
        return re.getBody();
    }

    private void printBracket(KOFieldDTO koFieldDTO) {
        RoundDTO round = koFieldDTO.getRound();
        System.out.println("-------- bracket -----------");
        while (round != null) {
            for (TournamentSingleGameDTO game : round.getGames()) {
                System.out.println("------------------");
                System.out.println(game.getPlayer1() != null ? game.getPlayer1().getFullName() : game.getGameName());
                System.out.println("                       --------------");
                System.out.println(game.getPlayer2() != null ? game.getPlayer2().getFullName() : "");
                System.out.println("------------------");
                System.out.println();
            }
            round = round.getNextRound();
        }
        System.out.println("-------- bracket -----------");
    }

    private RoundDTO getRound(KOFieldDTO koFieldDTO, int i) {
        if (i == 0) {
            return koFieldDTO.getRound();
        }
        if (i == 1) {
            return koFieldDTO.getRound().getNextRound();
        }
        if (i == 2) {
            return koFieldDTO.getRound().getNextRound().getNextRound();
        }
        if (i == 3) {
            return koFieldDTO.getRound().getNextRound().getNextRound().getNextRound();
        }
        if (i == 4) {
            return koFieldDTO.getRound().getNextRound().getNextRound().getNextRound().getNextRound();
        }
        if (i == 5) {
            return koFieldDTO.getRound().getNextRound().getNextRound().getNextRound().getNextRound().getNextRound();
        }
        if (i == 6) {
            return koFieldDTO.getRound().getNextRound().getNextRound().getNextRound().getNextRound().getNextRound().getNextRound();
        }
        throw new RuntimeException("#" + i);
    }

}