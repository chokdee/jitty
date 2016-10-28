package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.TournamentSingleGame;
import com.jmelzer.jitty.model.dto.GameSetDTO;
import com.jmelzer.jitty.model.dto.KOFieldDTO;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

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

            ResponseEntity<TournamentClassDTO> entity = http(HttpMethod.GET, "api/tournament-classes/1",
                    createHttpEntity(null, loginHeaders), TournamentClassDTO.class);

            assertNull(entity.getBody().getGroupCount());

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
            //start ko
            ResponseEntity<KOFieldDTO> koFieldEntity = http(HttpMethod.GET, "api/tournamentdirector/start-ko?id=1&assignPlayer=false",
                    createHttpEntity(entity.getBody(), loginHeaders), KOFieldDTO.class);
            printBracket(koFieldEntity.getBody());
            koFieldEntity = http(HttpMethod.GET, "api/tournamentdirector/start-ko?id=1&assignPlayer=true",
                    createHttpEntity(entity.getBody(), loginHeaders), KOFieldDTO.class);
            printBracket(koFieldEntity.getBody());



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