package com.jmelzer.jitty.rest;

import com.jmelzer.jitty.Application;
import com.jmelzer.jitty.model.dto.GameSetDTO;
import com.jmelzer.jitty.model.dto.TournamentClassDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

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
            assertThat(possibleGames.length, is(5));
            for (TournamentSingleGameDTO dto : possibleGames) {
                assertNotNull(dto.getPlayer1());
                assertNotNull(dto.getPlayer2());
                assertNotNull(dto.getGroup());
                assertNotNull(dto.getGroup().getClass());
            }

            voidEntity = http(HttpMethod.GET, "api/tournamentdirector/start-game?id=" + possibleGames[0].getId(),
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
            voidEntity = http(HttpMethod.GET, "api/tournamentdirector/start-game?id=" + possibleGames[1].getId(),
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
            voidEntity = http(HttpMethod.GET, "api/tournamentdirector/start-game?id=" + possibleGames[2].getId(),
                    createHttpEntity(entity.getBody(), loginHeaders), Void.class);
            assertTrue(voidEntity.getStatusCode().is2xxSuccessful());

            ResponseEntity<TournamentSingleGameDTO[]> runningGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/running-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertTrue(runningGamesEntity.getStatusCode().is2xxSuccessful());
            assertThat(runningGamesEntity.getBody().length, is(3));

//no more possible games, all are running
            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(2));


            TournamentSingleGameDTO[] runningGames = runningGamesEntity.getBody();
            for (TournamentSingleGameDTO runningGame : runningGames) {
                runningGame.addSet(new GameSetDTO(11, 9));
                runningGame.addSet(new GameSetDTO(11, 9));
                runningGame.addSet(new GameSetDTO(11, 9));
                voidEntity = http(HttpMethod.POST, "api/tournamentdirector/save-result",
                        createHttpEntity(runningGame, loginHeaders), Void.class);
                assertTrue(voidEntity.getStatusCode().is2xxSuccessful());
            }

            ResponseEntity<TournamentSingleGameDTO[]> finishedGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/finished-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(finishedGamesEntity.getBody().length, is(finishedFromDB + runningGames.length));

            possibleGamesEntity = http(HttpMethod.GET, "api/tournamentdirector/possible-games",
                    createHttpEntity(entity.getBody(), loginHeaders), TournamentSingleGameDTO[].class);
            assertThat(possibleGamesEntity.getBody().length, is(greaterThan(2)));



        } catch (HttpClientErrorException e) {
            System.out.println(e.getResponseBodyAsString());
            fail();
        }
    }

}