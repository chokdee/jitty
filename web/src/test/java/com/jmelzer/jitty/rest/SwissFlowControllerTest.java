/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

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

            final int PLAYERSIZE = 12;
            for (int i = 0; i < PLAYERSIZE; i++) {
                createPlayer(i, tournamentClass);
            }
            ResponseEntity<Void> voidEntity = http(HttpMethod.GET, "api/draw/select-phase-combination?cid=" + tClassId + "&type=5",
                    createHttpEntity(null, loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            ResponseEntity<SwissSystemPhaseDTO> phaseEntity = http(HttpMethod.GET, "api/draw/actual-phase?cid=" + tClassId,
                    createHttpEntity(null, loginHeaders), SwissSystemPhaseDTO.class);

            ResponseEntity<TournamentPlayerDTO[]> psEntity = http(HttpMethod.GET, "api/draw/possible-player-swiss-system?cid=" + tClassId,
                    createHttpEntity(null, loginHeaders), TournamentPlayerDTO[].class);
            assertThat(psEntity.getBody().length, is(12));
            ResponseEntity<TournamentSingleGameDTO[]> gamesEntity = http(HttpMethod.GET, "api/draw/swiss-draw?cid=" + tClassId,
                    createHttpEntity(null, loginHeaders), TournamentSingleGameDTO[].class);
            assertTrue(gamesEntity.getStatusCode().is2xxSuccessful());
            assertThat(gamesEntity.getBody().length, is(PLAYERSIZE / 2));

            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games", createHttpEntity(phaseEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(0));
            int possGames = 0;
            //auslosung  speichern und starten
            for (int i = 1; i <= 6; i++) {
                System.out.println("####################    start round " + (i) + " ####################");

                ResponseEntity<TournamentClassDTO> tcE = http(HttpMethod.GET, "api/tournament-classes/" + tClassId,
                        createHttpEntity(null, loginHeaders), TournamentClassDTO.class);
                assertThat(((SwissSystemPhaseDTO) (tcE.getBody().getSystem().getPhases().get(0))).getRound(), is(i));

                voidEntity = http(HttpMethod.POST, "api/draw/start-swiss-round?cid=" + tClassId,
                        createHttpEntity(gamesEntity.getBody(), loginHeaders), Void.class);
                assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

                assertThat(jdbcTemplate.queryForObject("select count(*) from tournament_group where id = " + phaseEntity.getBody().getGroup().getId(), Integer.class), is(1));

                possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                        createHttpEntity(phaseEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                assertThat(possibleGamesEntity.getBody().length, is(TABLE_COUNT));
                possGames += TABLE_COUNT;
                TournamentSingleGameDTO[] possibleGames = possibleGamesEntity.getBody();
                assertTrue(possibleGamesEntity.getStatusCode().is2xxSuccessful());

                //start the games we have found but max TABLE_COUNT
                ResponseEntity<Void> ve = http(HttpMethod.GET, "api/tournamentdirector/start-possible-games", createHttpEntity(null, loginHeaders), Void.class);
                assertTrue(ve.getStatusCode().is2xxSuccessful());

                //check the "running games" functionality
                runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                        createHttpEntity(phaseEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                assertTrue(runningGamesEntity.getStatusCode().is2xxSuccessful());
                assertThat(runningGamesEntity.getBody().length, is(TABLE_COUNT));

                //no more possible games, all are running
                possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                        createHttpEntity(phaseEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                //6 Tische und 6 laufen, d.h. 0 noch möglich
                assertThat(possibleGamesEntity.getBody().length, is(0));

                //enter results for all games
                TournamentSingleGameDTO[] runningGames = runningGamesEntity.getBody();
                addAndSaveResult(loginHeaders, tClassId, runningGames);

                //finished games must now the count of the entered results
                ResponseEntity<TournamentSingleGameDTO[]> finishedGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/finished-games",
                        createHttpEntity(phaseEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                assertThat(finishedGamesEntity.getBody().length, is(possGames));

            }


            assertThat(possGames, is(6 * TABLE_COUNT));

//            printBracket(koFieldEntity.getBody());
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