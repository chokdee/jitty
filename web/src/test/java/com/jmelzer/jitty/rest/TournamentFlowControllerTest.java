package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.TournamentSingleGame;
import com.jmelzer.jitty.model.dto.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

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

    @Test
    public void flow() throws Exception {
        try {
            HttpHeaders loginHeaders = doLogin();

            //create a tournament
            TournamentDTO tournament = new TournamentDTO();
            tournament.setName("Flowtest");
            tournament.setStartDate(new Date());
            tournament.setEndDate(new Date());
            ResponseEntity<Long> longEntitiy = http(HttpMethod.POST, "api/tournaments",
                    createHttpEntity(tournament, loginHeaders), Long.class);
            assertTrue(longEntitiy.getStatusCode().is2xxSuccessful());
            Long tId = longEntitiy.getBody();

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

            //now todo adding players to the class

            entity = http(HttpMethod.POST, "api/draw/calc-optimal-group-size",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);
            assertTrue(entity.getStatusCode().is2xxSuccessful());


            entity = http(HttpMethod.POST, "api/draw/automatic-draw",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentClassDTO.class);
            assertTrue(entity.getStatusCode().is2xxSuccessful());

            ResponseEntity<Void> voidEntity = http(HttpMethod.POST, "api/draw/save",
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            voidEntity = http(HttpMethod.GET, "api/draw/start?cid=1",
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            int finishedFromDB = http(HttpMethod.GET, "api/tournamentdirector/finished-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class).getBody().length;

            ResponseEntity<TournamentSingleGameDTO[]> possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            TournamentSingleGameDTO[] possibleGames = possibleGamesEntity.getBody();
            assertTrue(possibleGamesEntity.getStatusCode().is2xxSuccessful());
            //1 B Klasse and 3 A Klasse
            assertThat(possibleGames.length, is(4));
            for (TournamentSingleGameDTO dto : possibleGames) {
                assertNotNull(dto.getPlayer1());
                assertNotNull(dto.getPlayer2());
                assertNotNull(dto.getGroup());
                assertNotNull(dto.getGroup().getClass());
            }

            voidEntity = http(HttpMethod.GET, "api/tournamentdirector/start-game?id=" + possibleGames[1].getId(),
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
            voidEntity = http(HttpMethod.GET, "api/tournamentdirector/start-game?id=" + possibleGames[2].getId(),
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
            voidEntity = http(HttpMethod.GET, "api/tournamentdirector/start-game?id=" + possibleGames[3].getId(),
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            ResponseEntity<TournamentSingleGameDTO[]> runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertTrue(runningGamesEntity.getStatusCode().is2xxSuccessful());
            assertThat(runningGamesEntity.getBody().length, is(3));

//no more possible games, all are running
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(1));


            TournamentSingleGameDTO[] runningGames = runningGamesEntity.getBody();
            addResult(loginHeaders, runningGames);

            ResponseEntity<TournamentSingleGameDTO[]> finishedGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/finished-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(finishedGamesEntity.getBody().length, is(finishedFromDB + runningGames.length));

            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(greaterThan(2)));
            possibleGames = possibleGamesEntity.getBody();

            //do all games now
            int posGames = possibleGamesEntity.getBody().length;
            int maxGames = 18 - 3; //allready played above
            int counter = 0;
            while (posGames > 1) {

                int started = 0;
                for (int i = 0; i < posGames; i++) {
                    if (possibleGames[i].getTcName().equals("A-Klasse")) {
                        voidEntity = http(HttpMethod.GET, "api/tournamentdirector/start-game?id=" + possibleGames[i].getId(),
                                createHttpEntity(entity.getBody(), loginHeaders), Void.class);
                        assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
                        started++;
                    }

                }

                runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                        createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                assertTrue(runningGamesEntity.getStatusCode().is2xxSuccessful());
                assertThat(runningGamesEntity.getBody().length, is(started));

                runningGames = runningGamesEntity.getBody();
                counter += addResult(loginHeaders, runningGames);
                assertTrue("must be smaller or equals" + counter, counter <= maxGames);


                possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                        createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
                posGames = possibleGamesEntity.getBody().length;
                possibleGames = possibleGamesEntity.getBody();

                ResponseEntity<Boolean> finished = http(HttpMethod.GET, "api/tournamentdirector/groups-finished?id=1",
                        createHttpEntity(entity.getBody(), loginHeaders), Boolean.class);
                if (maxGames == counter) {
                    assertTrue(finished.getBody());
                    ResponseEntity<Long[]> finishedClzs = http(HttpMethod.GET, "api/tournamentdirector/any-phase-finished",
                            createHttpEntity(entity.getBody(), loginHeaders), Long[].class);
                    assertTrue(finishedClzs.getBody().length == 1);
                    assertTrue(finishedClzs.getBody()[0]== 1L);

                } else {
                    assertFalse(finished.getBody());
                    ResponseEntity<Long[]> finishedClzs = http(HttpMethod.GET, "api/tournamentdirector/any-phase-finished",
                            createHttpEntity(entity.getBody(), loginHeaders), Long[].class);
                    assertTrue(finishedClzs.getBody().length == 0);
                }

            }
//            possible-player-for-kofield
            ResponseEntity<TournamentPlayerDTO[]> avaiPlayerEntity = http(HttpMethod.GET, "api/draw/possible-player-for-kofield?cid=1",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentPlayerDTO[].class);
            assertTrue(avaiPlayerEntity.getStatusCode().is2xxSuccessful());
            //8 player must be avaiable for seeding
            assertThat(avaiPlayerEntity.getBody().length , is (8));

            //start ko
            ResponseEntity<KOFieldDTO> koFieldEntity = http(HttpMethod.GET, "api/draw/draw-ko?id=1&assignPlayer=false",
                    createHttpEntity(entity.getBody(), loginHeaders), KOFieldDTO.class);
            printBracket(koFieldEntity.getBody());
            koFieldEntity = http(HttpMethod.GET, "api/draw/draw-ko?id=1&assignPlayer=true",
                    createHttpEntity(entity.getBody(), loginHeaders), KOFieldDTO.class);
            printBracket(koFieldEntity.getBody());

            //no more player must be avaiable for seeding
            avaiPlayerEntity = http(HttpMethod.GET, "api/draw/possible-player-for-kofield?cid=1",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentPlayerDTO[].class);
            assertTrue(avaiPlayerEntity.getStatusCode().is2xxSuccessful());
            assertThat(avaiPlayerEntity.getBody().length , is (0));

            //start the ko round now
            voidEntity = http(HttpMethod.GET, "api/draw/start-ko?cid=1",
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            //4 games must be possible now (one is from B-Klasse
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(4));


        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }
    private void printBracket(KOFieldDTO koFieldDTO) {
        System.out.println("-------- bracket -----------");
        for (TournamentSingleGameDTO game : koFieldDTO.getRound().getGames()) {
            System.out.println("------------------");
            System.out.println(game.getPlayer1().getFullName());
            System.out.println("                       --------------");
            System.out.println(game.getPlayer2().getFullName());
            System.out.println("------------------");
            System.out.println();
        }
        System.out.println("-------- bracket -----------");
    }
    private int addResult(HttpHeaders loginHeaders, TournamentSingleGameDTO[] runningGames) {
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