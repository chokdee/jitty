package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.dto.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 26.07.2016.
 * Test draw controller
 */
@SuppressWarnings("OverlyComplexMethod")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TournamentFlowControllerTest extends SecureResourceTest {
    HttpHeaders loginHeaders;

    @Test
    public void flow() throws Exception {
        try {
            loginHeaders = doLogin();

            ResponseEntity<TournamentSingleGameDTO[]> possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(null, loginHeaders), TournamentSingleGameDTO[].class);
            int possibleGamesBeforeStartTest = possibleGamesEntity.getBody().length;
            ResponseEntity<TournamentSingleGameDTO[]> runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                    createHttpEntity(null, loginHeaders), TournamentSingleGameDTO[].class);
            int runningGamesBeforeStartTest = runningGamesEntity.getBody().length;
            //Turnier laeuft, es gibt in der db aber bereits ein paar moegliche und beendete Spiele, die zaehlen wir hier
            int finishedGamesBeforeStartTest = http(HttpMethod.GET, "api/tournamentdirector/finished-games",
                    createHttpEntity(null, loginHeaders), TournamentSingleGameDTO[].class).getBody().length;

            Long tId = createTournament();

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);


            //create a class
            TournamentClassDTO tournamentClass = createClz(tId);
            long tClassId = tournamentClass.getId();

            final int PLAYERSIZE = 32;
            for (int i = 0; i < PLAYERSIZE; i++) {
                createPlayer(i, tournamentClass);
            }
            final int GROUP_COUNT = (int)Math.round(PLAYERSIZE/4.);
            ResponseEntity<TournamentClassDTO> entity = http(HttpMethod.POST, "api/draw/calc-optimal-group-size",
                    createHttpEntity(tournamentClass, loginHeaders), TournamentClassDTO.class);
            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().getPlayerPerGroup(), is(4));
            assertThat(entity.getBody().getGroupCount(), is(GROUP_COUNT));

            //auslosung
            entity = http(HttpMethod.POST, "api/draw/automatic-draw",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);
            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().getGroups().size(), is(GROUP_COUNT));
            assertThat(entity.getBody().getGroupCount(), is(GROUP_COUNT));

            assertThat(jdbcTemplate.queryForObject("select count(*) from tournament_group where tc_id = " + tClassId, Integer.class), is(0));
            //speichern
            entity = http(HttpMethod.POST, "api/draw/save",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);
            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().getGroups().size(), is(GROUP_COUNT));
            assertThat(entity.getBody().getGroupCount(), is(GROUP_COUNT));
            assertThat(jdbcTemplate.queryForObject("select count(*) from tournament_group where tc_id = " + tClassId, Integer.class), is(GROUP_COUNT));

            //starten
            ResponseEntity<Void> voidEntity = http(HttpMethod.GET, "api/draw/start?cid=" + tClassId,
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());


            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            TournamentSingleGameDTO[] possibleGames = possibleGamesEntity.getBody();
            assertTrue(possibleGamesEntity.getStatusCode().is2xxSuccessful());

            //2 3er Gruppen & 2 4er Gruppen == 2 + 4 = 6 moegliche Spiele
            //todo calc it not hardcoded
//            assertThat(possibleGames.length, is(6 + possibleGamesBeforeStartTest));
            int gamesCount = 0;
            List<TournamentSingleGameDTO> gamesForTestClz = new ArrayList<>();
            for (TournamentSingleGameDTO dto : possibleGames) {
                assertNotNull(dto.getPlayer1());
                assertNotNull(dto.getPlayer2());
                assertNotNull(dto.getGroup());
                assertNotNull(dto.getGroup().getClass());
                if (dto.getTcName().equals("dummy")) {
                    gamesForTestClz.add(dto);
                    gamesCount++;
                }
            }

            for (TournamentSingleGameDTO tournamentSingleGameDTO : gamesForTestClz) {
                startGame(tournamentSingleGameDTO.getId());
            }

            runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertTrue(runningGamesEntity.getStatusCode().is2xxSuccessful());
            assertThat(runningGamesEntity.getBody().length, is(gamesForTestClz.size() + runningGamesBeforeStartTest));

//no more possible games, all are running
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(possibleGamesBeforeStartTest));


            TournamentSingleGameDTO[] runningGames = runningGamesEntity.getBody();
            addAndSaveResult(loginHeaders, runningGames);

            ResponseEntity<TournamentSingleGameDTO[]> finishedGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/finished-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(finishedGamesEntity.getBody().length, is(finishedGamesBeforeStartTest + runningGames.length));

            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(greaterThan(2)));
            possibleGames = possibleGamesEntity.getBody();

            //do all games now
            int possibleGamesCounter = possibleGamesEntity.getBody().length;
            //2x 3er = 2x*3 + 2x 4er = 2x*6
            int maxGames = GROUP_COUNT * (3 * 2) - gamesCount; //allready played above
            int counter = 0;
            while (possibleGamesCounter - possibleGamesBeforeStartTest > 0) {

                int started = 0;
                for (int i = 0; i < possibleGamesCounter; i++) {
                    if (possibleGames[i].getTcName().equals("dummy")) {
                        startGame(possibleGames[i].getId());
                        started++;
                    }
                }

                runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                        createHttpEntity(null, loginHeaders), TournamentSingleGameDTO[].class);
                assertTrue(runningGamesEntity.getStatusCode().is2xxSuccessful());
                assertThat(runningGamesEntity.getBody().length, is(started));

                runningGames = runningGamesEntity.getBody();
                counter += addAndSaveResult(loginHeaders, runningGames);
                assertThat(counter, is(lessThanOrEqualTo(maxGames)));


                possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                        createHttpEntity(null, loginHeaders), TournamentSingleGameDTO[].class);
                possibleGamesCounter = possibleGamesEntity.getBody().length;
                possibleGames = possibleGamesEntity.getBody();

                ResponseEntity<Boolean> finished = http(HttpMethod.GET, "api/tournamentdirector/groups-finished?id=" + tClassId,
                        createHttpEntity(entity.getBody(), loginHeaders), Boolean.class);

                if (maxGames == counter) {
                    finished = http(HttpMethod.GET, "api/tournamentdirector/groups-finished?id=" + tClassId,
                            createHttpEntity(entity.getBody(), loginHeaders), Boolean.class);
                    assertTrue(finished.getBody());
                    ResponseEntity<Long[]> finishedClzs = http(HttpMethod.GET, "api/tournamentdirector/any-phase-finished",
                            createHttpEntity(entity.getBody(), loginHeaders), Long[].class);
                    assertTrue(finishedClzs.getBody().length == 1);
                    assertThat(finishedClzs.getBody()[0], is(tClassId));

                } else {
                    assertFalse(finished.getBody());
                    ResponseEntity<Long[]> finishedClzs = http(HttpMethod.GET, "api/tournamentdirector/any-phase-finished",
                            createHttpEntity(tournamentClass, loginHeaders), Long[].class);
                    assertTrue(finishedClzs.getBody().length == 0);
                }
                System.out.println("running " + runningGames.length);
                System.out.println("finished " + counter);
                System.out.println("possible " + possibleGamesCounter);

            }
//            possible-player-for-kofield
            ResponseEntity<TournamentPlayerDTO[]> avaiPlayerEntity = http(HttpMethod.GET, "api/draw/possible-player-for-kofield?cid=" + tClassId,
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentPlayerDTO[].class);
            assertTrue(avaiPlayerEntity.getStatusCode().is2xxSuccessful());
            //8 player must be avaiable for seeding
            assertThat(avaiPlayerEntity.getBody().length, is(GROUP_COUNT*2));

            //start ko
            ResponseEntity<KOFieldDTO> koFieldEntity = http(HttpMethod.GET, "api/draw/draw-ko?id=" + tClassId + "&assignPlayer=false",
                    createHttpEntity(entity.getBody(), loginHeaders), KOFieldDTO.class);
            printBracket(koFieldEntity.getBody());
            koFieldEntity = http(HttpMethod.GET, "api/draw/draw-ko?id=" + tClassId + "&assignPlayer=true",
                    createHttpEntity(entity.getBody(), loginHeaders), KOFieldDTO.class);
            printBracket(koFieldEntity.getBody());

            //no more player must be avaiable for seeding
            avaiPlayerEntity = http(HttpMethod.GET, "api/draw/possible-player-for-kofield?cid=" + tClassId,
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentPlayerDTO[].class);
            assertTrue(avaiPlayerEntity.getStatusCode().is2xxSuccessful());
            assertThat(avaiPlayerEntity.getBody().length, is(0));

            //start the ko round now
            voidEntity = http(HttpMethod.GET, "api/draw/start-ko?cid=" + tClassId,
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            //4 games must be possible now (one is from B-Klasse
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is((GROUP_COUNT*2/2) + possibleGamesBeforeStartTest));
            possibleGames = possibleGamesEntity.getBody();

            for (TournamentSingleGameDTO dto : possibleGames) {
                assertNotNull(dto.getPlayer1());
                assertNotNull(dto.getPlayer2());
                if (dto.getTcName().equals("dummy")) {
                    assertNull(dto.getGroup());
                    startGame(dto.getId());
                    addAndSaveResult(loginHeaders, new TournamentSingleGameDTO[]{dto});
                }
            }

            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(GROUP_COUNT/2 + possibleGamesBeforeStartTest));

            possibleGames = possibleGamesEntity.getBody();

            for (TournamentSingleGameDTO dto : possibleGames) {
                assertNotNull(dto.getPlayer1());
                assertNotNull(dto.getPlayer2());
                if (dto.getTcName().equals("dummy")) {
                    assertNull(dto.getGroup());
                    startGame(dto.getId());
                    addAndSaveResult(loginHeaders, new TournamentSingleGameDTO[]{dto});
                }
            }
            //final
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(GROUP_COUNT/4 + possibleGamesBeforeStartTest));

            possibleGames = possibleGamesEntity.getBody();

            for (TournamentSingleGameDTO dto : possibleGames) {
                assertNotNull(dto.getPlayer1());
                assertNotNull(dto.getPlayer2());
                if (dto.getTcName().equals("dummy")) {
                    assertNull(dto.getGroup());
                    startGame(dto.getId());
                    addAndSaveResult(loginHeaders, new TournamentSingleGameDTO[]{dto});
                }
            }
            //final completed
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(possibleGamesBeforeStartTest));

            koFieldEntity = http(HttpMethod.GET, "api/draw/get-ko?cid=" + tClassId, createHttpEntity(null, loginHeaders), KOFieldDTO.class);
            printBracket(koFieldEntity.getBody());


        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

    private TournamentClassDTO createClz(Long tId) {
        TournamentClassDTO tournamentClass = new TournamentClassDTO();
        tournamentClass.setName("dummy");
        tournamentClass.setStartTTR(0);
        tournamentClass.setEndTTR(3000);

        ResponseEntity<Long> longEntitiy = http(HttpMethod.POST, "api/tournament-classes/" + tId,
                createHttpEntity(tournamentClass, loginHeaders), Long.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
        Long tClassId = longEntitiy.getBody();
        assertNotNull(tClassId);

        ResponseEntity<TournamentClassDTO> entity = http(HttpMethod.GET, "api/tournament-classes/" + tClassId,
                createHttpEntity(null, loginHeaders), TournamentClassDTO.class);
        tournamentClass = entity.getBody();
        assertNull("still not drawed", tournamentClass.getGroupCount());
        return tournamentClass;
    }

    private Long createTournament() {
        //create a tournament
        TournamentDTO tournament = new TournamentDTO();
        tournament.setName("Flowtest");
        tournament.setStartDate(new Date());
        tournament.setEndDate(new Date());
        ResponseEntity<Long> longEntitiy = http(HttpMethod.POST, "api/tournaments",
                createHttpEntity(tournament, loginHeaders), Long.class);
        assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
        Long tId = longEntitiy.getBody();

        http(HttpMethod.GET, "api/tournaments/actual/" + tId, createHttpEntity(tournament, loginHeaders), Void.class);
        return tId;
    }

    private void startGame(Long gameId) {
        ResponseEntity voidEntity = http(HttpMethod.GET, "api/tournamentdirector/start-game?id=" + gameId,
                createHttpEntity(null, loginHeaders), Void.class);
        assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
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

    private int addAndSaveResult(HttpHeaders loginHeaders, TournamentSingleGameDTO[] runningGames) {
        ResponseEntity<Long> longResponseEntity;
        String sql = "select ID, POINTS1, POINTS2  from TOURNAMENT_SINGLE_GAME_SET gs, GAME_SET g where gs.SETS_ID = g.id and gs.TOURNAMENT_SINGLE_GAME_ID =  ";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        for (TournamentSingleGameDTO runningGame : runningGames) {
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

}