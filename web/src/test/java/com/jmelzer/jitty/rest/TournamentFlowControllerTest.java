/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.model.TournamentSystemType;
import com.jmelzer.jitty.model.dto.*;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

import static com.jmelzer.jitty.rest.TestUtil.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 26.07.2016.
 * Test draw controller
 */
@SuppressWarnings("OverlyComplexMethod")
public class TournamentFlowControllerTest extends SecureResourceTest {

    public static final String TC_NAME = "dummy";

    static int TABLE_COUNT = 8;


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

            final int PLAYERSIZE = 32;
            for (int i = 0; i < PLAYERSIZE; i++) {
                createPlayer(i, tournamentClass);
            }
            final int GROUP_COUNT = (int) Math.round(PLAYERSIZE / 4.);

            ResponseEntity<Void> voidEntity = http(HttpMethod.GET, "api/draw/select-phase-combination?cid=" + tClassId + "&type=1",
                    createHttpEntity(null, loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            ResponseEntity<Boolean> booleanEntity = http(HttpMethod.GET, "api/draw/activate-phase?cid=" + tClassId + "&type=1",
                    createHttpEntity(null, loginHeaders), Boolean.class);
            assertTrue(booleanEntity.getStatusCode().is2xxSuccessful());
            assertTrue(booleanEntity.getBody());

            ResponseEntity<GroupPhaseDTO> gpEntity = http(HttpMethod.GET, "api/draw/actual-phase?cid=" + tClassId,
                    createHttpEntity(null, loginHeaders), GroupPhaseDTO.class);
            assertNotNull(gpEntity.getBody());

            gpEntity = http(HttpMethod.POST, "api/draw/calc-optimal-group-size",
                    createHttpEntity(gpEntity.getBody(), loginHeaders), GroupPhaseDTO.class);
            assertTrue(gpEntity.getStatusCode().is2xxSuccessful());
            assertThat(gpEntity.getBody().getPlayerPerGroup(), is(4));
            assertThat(gpEntity.getBody().getGroupCount(), is(GROUP_COUNT));

            //auslosung und speichern
            gpEntity = http(HttpMethod.POST, "api/draw/automatic-draw?cid=" + tClassId,
                    createHttpEntity(gpEntity.getBody(), loginHeaders), GroupPhaseDTO.class);
            assertTrue(gpEntity.getStatusCode().is2xxSuccessful());
            assertThat(gpEntity.getBody().getGroups().size(), is(GROUP_COUNT));
            assertThat(gpEntity.getBody().getGroupCount(), is(GROUP_COUNT));
            assertThat(gpEntity.getBody().getGroups().get(0).getPlayers().size(), is(4));

            assertThat(jdbcTemplate.queryForObject("select count(*) from tournament_group where gp_id = " + gpEntity.getBody().getId(), Integer.class), is(GROUP_COUNT));

            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games", createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(possibleGamesBeforeStartTest));

            //starten
            voidEntity = http(HttpMethod.GET, "api/draw/start?cid=" + tClassId,
                    createHttpEntity(gpEntity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());


            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            TournamentSingleGameDTO[] possibleGames = possibleGamesEntity.getBody();
            assertTrue(possibleGamesEntity.getStatusCode().is2xxSuccessful());

            // 32 spieler   -  8 Gruppen = 16 mögliche spiele
            assertThat(possibleGames.length, is(16));
            int firstRound = 8;
            //2 3er Gruppen & 2 4er Gruppen == 2 + 4 = 6 moegliche Spiele
            //todo calc it not hardcoded
//            assertThat(possibleGames.length, is(6 + possibleGamesBeforeStartTest));


            //start the games we have found but max TABLE_COUNT
            ResponseEntity<Void> ve = http(HttpMethod.GET, "api/tournamentdirector/start-possible-games", createHttpEntity(null, loginHeaders), Void.class);
            assertTrue(ve.getStatusCode().is2xxSuccessful());
//            for (TournamentSingleGameDTO tournamentSingleGameDTO : gamesForTestClz) {
//                startGame(tournamentSingleGameDTO.getId());
//            }

            //check the "running games" functionality
            runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                    createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertTrue(runningGamesEntity.getStatusCode().is2xxSuccessful());
            assertThat(runningGamesEntity.getBody().length, is(TABLE_COUNT));

            //no more possible games, all are running
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            //8 Tische und 8 laufen, d.h. 8 noch möglich
            assertThat(possibleGamesEntity.getBody().length, is(8));

            //enter results for all games
            TournamentSingleGameDTO[] runningGames = runningGamesEntity.getBody();
            addAndSaveResult(loginHeaders, tClassId, runningGames);

            //finished games must now the count of the entered results
            ResponseEntity<TournamentSingleGameDTO[]> finishedGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/finished-games",
                    createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(finishedGamesEntity.getBody().length, is(finishedGamesBeforeStartTest + runningGames.length));

            //some games must be possible
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(greaterThan(2)));
            possibleGames = possibleGamesEntity.getBody();

            //do all games now
            int possibleGamesCounter = possibleGamesEntity.getBody().length;
            //2x 3er = 2x*3 + 2x 4er = 2x*6
            int maxGames = GROUP_COUNT * (3 * 2) - firstRound; //allready played above
            int counter = 0;
            while (possibleGamesCounter - possibleGamesBeforeStartTest > 0) {

                int started = 0;
                for (int i = 0; i < possibleGamesCounter && i < TABLE_COUNT; i++) {
                    startGame(possibleGames[i].getId());
                    started++;
                }

                runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                        createHttpEntity(null, loginHeaders), TournamentSingleGameDTO[].class);
                assertTrue(runningGamesEntity.getStatusCode().is2xxSuccessful());
                assertThat(runningGamesEntity.getBody().length, is(started));

                runningGames = runningGamesEntity.getBody();
                counter += addAndSaveResult(loginHeaders, tClassId, runningGames);
                assertThat(counter, is(lessThanOrEqualTo(maxGames)));


                possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                        createHttpEntity(null, loginHeaders), TournamentSingleGameDTO[].class);
                possibleGamesCounter = possibleGamesEntity.getBody().length;
                possibleGames = possibleGamesEntity.getBody();

                ResponseEntity<Boolean> finished = http(HttpMethod.GET, "api/tournamentdirector/groups-finished?id=" + tClassId,
                        createHttpEntity(gpEntity.getBody(), loginHeaders), Boolean.class);

                if (maxGames == counter) {
                    finished = http(HttpMethod.GET, "api/tournamentdirector/groups-finished?id=" + tClassId,
                            createHttpEntity(gpEntity.getBody(), loginHeaders), Boolean.class);
                    assertTrue(finished.getBody());
                    ResponseEntity<Long[]> finishedClzs = http(HttpMethod.GET, "api/tournamentdirector/any-phase-finished",
                            createHttpEntity(gpEntity.getBody(), loginHeaders), Long[].class);
                    assertTrue(finishedClzs.getBody().length == 1);
                    assertThat(finishedClzs.getBody()[0], is(tClassId));

                } else {
                    assertFalse(finished.getBody());
                    ResponseEntity<Long[]> finishedClzs = http(HttpMethod.GET, "api/tournamentdirector/any-phase-finished",
                            createHttpEntity(tournamentClass, loginHeaders), Long[].class);
                    assertTrue(finishedClzs.getBody().length == 0);
                }
                System.out.println("---------------------------------");
                System.out.println("running " + runningGames.length);
                System.out.println("finished " + counter);
                System.out.println("possible " + possibleGamesCounter);
                System.out.println("---------------------------------");

            }

            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games", createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(possibleGamesBeforeStartTest));

//            possible-player-for-kofield
            ResponseEntity<TournamentPlayerDTO[]> avaiPlayerEntity = http(HttpMethod.GET, "api/draw/possible-player-for-kofield?cid=" + tClassId,
                    createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentPlayerDTO[].class);
            assertTrue(avaiPlayerEntity.getStatusCode().is2xxSuccessful());
            //8 player must be avaiable for seeding
            assertThat(avaiPlayerEntity.getBody().length, is(GROUP_COUNT * 2));

            //start ko
            ResponseEntity<KOFieldDTO> koFieldEntity = http(HttpMethod.GET, "api/draw/draw-ko?id=" + tClassId + "&assignPlayer=false",
                    createHttpEntity(gpEntity.getBody(), loginHeaders), KOFieldDTO.class);
            printBracket(koFieldEntity.getBody());
            koFieldEntity = http(HttpMethod.GET, "api/draw/draw-ko?id=" + tClassId + "&assignPlayer=true",
                    createHttpEntity(gpEntity.getBody(), loginHeaders), KOFieldDTO.class);
            printBracket(koFieldEntity.getBody());

            //no more player must be avaiable for seeding
            avaiPlayerEntity = http(HttpMethod.GET, "api/draw/possible-player-for-kofield?cid=" + tClassId,
                    createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentPlayerDTO[].class);
            assertTrue(avaiPlayerEntity.getStatusCode().is2xxSuccessful());
            assertThat(avaiPlayerEntity.getBody().length, is(0));

            //start the ko round now
            voidEntity = http(HttpMethod.GET, "api/draw/start-ko?cid=" + tClassId, createHttpEntity(gpEntity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            //check the possible games and do a loop for all rounds
            ResponseEntity<KOFieldDTO> koEntity = http(HttpMethod.GET, "api/draw/get-ko?cid=" + tClassId, createHttpEntity(null, loginHeaders), KOFieldDTO.class);
            assertTrue(koEntity.getStatusCode().is2xxSuccessful());
            int noOfRounds = koEntity.getBody().getNoOfRounds();
            assertThat(noOfRounds, is(greaterThan(2)));

            for (int i = 0; i < noOfRounds; i++) {

                RoundDTO r = getRound(koEntity.getBody(), i);
                System.out.println("Round #" + i + " -- " + r.getRoundType().getName());
                possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games", createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                assertThat(possibleGamesEntity.getBody().length, is((r.getGameSize()) + possibleGamesBeforeStartTest));
                possibleGames = possibleGamesEntity.getBody();

                for (TournamentSingleGameDTO dto : possibleGames) {
                    assertNotNull(dto.getPlayer1());
                    assertNotNull(dto.getPlayer2());
                    if (dto.getTcName().equals(TC_NAME)) {
                        assertNull(dto.getGroup());
                        dto = startGame(dto.getId());
                        addAndSaveResult(loginHeaders, tClassId, new TournamentSingleGameDTO[]{dto});
                    }
                }
            }

            //final completed
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(gpEntity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(possibleGamesBeforeStartTest));

            koFieldEntity = http(HttpMethod.GET, "api/draw/get-ko?cid=" + tClassId, createHttpEntity(null, loginHeaders), KOFieldDTO.class);
//            printBracket(koFieldEntity.getBody());
            System.out.println("all rounds completed, yeah");


        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    private Long createTournament() {
        return TestUtil.createTournament("Flowtest", TABLE_COUNT, 1);
    }

    private TournamentClassDTO createClz(Long tId) {
        return TestUtil.createClz(tId, TC_NAME, TournamentSystemType.GK);
    }

    private void createPlayer(int i, TournamentClassDTO tournamentClass) {
        TestUtil.createPlayer(i, tournamentClass);
    }

    private int addAndSaveResult(HttpHeaders loginHeaders, Long cid, TournamentSingleGameDTO[] runningGames) {
        ResponseEntity<Long> longResponseEntity;
        String sql = "select ID, POINTS1, POINTS2  from TOURNAMENT_SINGLE_GAME_SET gs, GAME_SET g where gs.SETS_ID = g.id and gs.TOURNAMENT_SINGLE_GAME_ID =  ";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        for (TournamentSingleGameDTO runningGame : runningGames) {
            System.out.println("runningGame.getTableNo() = " + runningGame.getTableNo());
            runningGame.addSet(new GameSetDTO(11, 2));
            runningGame.addSet(new GameSetDTO(11, 2));
            runningGame.addSet(new GameSetDTO(11, 2));
            longResponseEntity = http(HttpMethod.POST, "api/tournamentdirector/save-result",
                    createHttpEntity(runningGame, loginHeaders), Long.class);
            assertTrue(longResponseEntity.getStatusCode().is2xxSuccessful());
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql + longResponseEntity.getBody());
            for (Map<String, Object> setsMap : list) {
                Integer p1 = (Integer) setsMap.get("POINTS1");
                assertThat(p1, is(11));
                Integer p2 = (Integer) setsMap.get("POINTS2");
                assertThat(p2, is(2));
            }

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