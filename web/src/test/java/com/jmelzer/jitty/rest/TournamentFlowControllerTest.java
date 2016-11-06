package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.dto.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 26.07.2016.
 * Test draw controller
 */
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

            //create a class
            TournamentClassDTO tournamentClass = new TournamentClassDTO();
            tournamentClass.setName("dummy");
            tournamentClass.setStartTTR(0);
            tournamentClass.setEndTTR(3000);

            longEntitiy = http(HttpMethod.POST, "api/tournament-classes/" + tId,
                    createHttpEntity(tournamentClass, loginHeaders), Long.class);
            assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
            Long tClassId = longEntitiy.getBody();
            assertNotNull(tClassId);

            ResponseEntity<TournamentClassDTO> entity = http(HttpMethod.GET, "api/tournament-classes/" + tClassId,
                    createHttpEntity(null, loginHeaders), TournamentClassDTO.class);
            tournamentClass = entity.getBody();
            assertNull("still not drawed", tournamentClass.getGroupCount());

            for (int i = 0; i < 14; i++) {
                createPlayer(i, tournamentClass);
            }

            entity = http(HttpMethod.POST, "api/draw/calc-optimal-group-size",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);
            assertTrue(entity.getStatusCode().is2xxSuccessful());
            assertThat(entity.getBody().getPlayerPerGroup(), is(4));
            assertThat(entity.getBody().getGroupCount(), is(4));

            //auslosung
            entity = http(HttpMethod.POST, "api/draw/automatic-draw",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);
            assertTrue(entity.getStatusCode().is2xxSuccessful());

            //speichern
            ResponseEntity<Void> voidEntity = http(HttpMethod.POST, "api/draw/save",
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            //starten
            voidEntity = http(HttpMethod.GET, "api/draw/start?cid=" + tClassId,
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());


            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            TournamentSingleGameDTO[] possibleGames = possibleGamesEntity.getBody();
            assertTrue(possibleGamesEntity.getStatusCode().is2xxSuccessful());

            //2 3er Gruppen & 2 4er Gruppen == 2 + 4 = 6 moegliche Spiele
            assertThat(possibleGames.length, is(6 + possibleGamesBeforeStartTest));
            List<TournamentSingleGameDTO> gamesForTestClz = new ArrayList<>();
            for (TournamentSingleGameDTO dto : possibleGames) {
                assertNotNull(dto.getPlayer1());
                assertNotNull(dto.getPlayer2());
                assertNotNull(dto.getGroup());
                assertNotNull(dto.getGroup().getClass());
                if (dto.getTcName().equals("dummy")) {
                    gamesForTestClz.add(dto);
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
            int maxGames = 18 - 6; //allready played above
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
                assertTrue("must be smaller or equals" + counter, counter <= maxGames);


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
                    assertThat(finishedClzs.getBody()[0] , is(tClassId));

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
            assertThat(avaiPlayerEntity.getBody().length, is(8));

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
            assertThat(possibleGamesEntity.getBody().length, is(4 + possibleGamesBeforeStartTest));
            possibleGames = possibleGamesEntity.getBody();

            for (TournamentSingleGameDTO dto : possibleGames) {
                assertNotNull(dto.getPlayer1());
                assertNotNull(dto.getPlayer2());
                if (dto.getTcName().equals("dummy")) {
                    assertNull(dto.getGroup());
                    startGame(dto.getId());
                    addAndSaveResult(loginHeaders, new TournamentSingleGameDTO[] {dto});
                }
            }

            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(2 + possibleGamesBeforeStartTest));



        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
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
        System.out.println("-------- bracket -----------");
        for (TournamentSingleGameDTO game : koFieldDTO.getRound().getGames()) {
            System.out.println("------------------");
            System.out.println(game.getPlayer1() != null ? game.getPlayer1().getFullName() : game.getGameName());
            System.out.println("                       --------------");
            System.out.println(game.getPlayer2() != null ? game.getPlayer2().getFullName() : "");
            System.out.println("------------------");
            System.out.println();
        }
        System.out.println("-------- bracket -----------");
    }

    private int addAndSaveResult(HttpHeaders loginHeaders, TournamentSingleGameDTO[] runningGames) {
        ResponseEntity<Void> voidEntity;
        for (TournamentSingleGameDTO runningGame : runningGames) {
            runningGame.addSet(new GameSetDTO(11, 9));
            runningGame.addSet(new GameSetDTO(11, 9));
            runningGame.addSet(new GameSetDTO(11, 9));
            voidEntity = http(HttpMethod.POST, "api/tournamentdirector/save-result",
                    createHttpEntity(runningGame, loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
        }
        return runningGames.length;
    }

}