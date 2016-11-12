package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.GameSetDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by J. Melzer on 11.06.2016.
 */
public class TournamentServiceTest {
    TournamentService service = new TournamentService();

    @Before
    public void setup() {
        service = new TournamentService();
    }

    @Test
    public void testAddPossibleGamesToQueue() throws Exception {

        TournamentGroup group = prepareGroupWithPlayerAndGames();
        List<TournamentGroup> groups = new ArrayList<>();
        groups.add(group);
        service.addPossibleGroupGamesToQueue(groups);
        assertEquals(2, service.getQueueSize());

        TournamentSingleGame game = service.poll();
        game.setCalled(true);
        assertEquals(1, service.getQueueSize());
        service.addPossibleGroupGamesToQueue(groups);
        assertEquals("player busy no new player can be added", 1, service.getQueueSize());

        game = service.poll();
        game.setCalled(true);
        assertEquals(0, service.getQueueSize());

        service.addPossibleGroupGamesToQueue(groups);
        assertEquals(2, service.getQueueSize());
    }


    private TournamentGroup prepareGroupWithPlayerAndGames() {
        TournamentGroup group = new TournamentGroup("A");
        group.addPlayer(new TournamentPlayer(1L, "1", "1"));
        group.addPlayer(new TournamentPlayer(2L, "2", "2"));
        group.addPlayer(new TournamentPlayer(3L, "3", "3"));
        group.addPlayer(new TournamentPlayer(4L, "4", "4"));


        addGame(group, 0, 1);
        addGame(group, 0, 2);
        addGame(group, 0, 3);
        addGame(group, 1, 2);
        addGame(group, 1, 3);
        addGame(group, 2, 3);
        return group;
    }

    private TournamentGroup prepareGroupWith3PlayerAndGames() {
        TournamentGroup group = new TournamentGroup("A");
        group.addPlayer(new TournamentPlayer(1L, "1", "1"));
        group.addPlayer(new TournamentPlayer(2L, "2", "2"));
        group.addPlayer(new TournamentPlayer(3L, "3", "3"));


        addGame(group, 0, 1);
        addGame(group, 0, 2);
        addGame(group, 1, 2);
        return group;
    }

    @Test
    public void calcRankingForGroup() {
        TournamentGroup group = prepareGroupWithPlayerAndGames();
        //first player 3:0
        //second ... last 1:2
        for (int i = 0; i < 6; i++) {
            TournamentSingleGame game = group.getGames().get(i);
            if (i < 3) {
                game.setWinner(1);
            }
        }
        service.calcRankingForGroup(group);

        group = prepareGroupWith3PlayerAndGames();
        service.calcRankingForGroup(group);
        assertThat(group.getRanking().size(), is(3));
        for (PlayerStatistic playerStatistic : group.getRanking()) {
            System.out.println("playerStatistic = " + playerStatistic);
        }
    }

    private void addGame(TournamentGroup group, int i, int j) {
        TournamentSingleGame game;
        game = new TournamentSingleGame();
        game.setPlayer1(group.getPlayers().get(i));
        game.setPlayer2(group.getPlayers().get(j));
        group.addGame(game);
    }

    @Test
    public void calcWinner() {
        TournamentSingleGameDTO game = new TournamentSingleGameDTO();
        game.addSet(new GameSetDTO(11, 9));
        assertThat(service.calcWinner(game).getWinner(), is(1));
        game.addSet(new GameSetDTO(11, 13));
        try {
            service.calcWinner(game);
            fail("no winner");
        } catch (IllegalArgumentException e) {
            //ok
        }
        game.addSet(new GameSetDTO(11, 13));
        assertThat(service.calcWinner(game).getWinner(), is(2));
    }
    @Test
    public void getNotRunning() {
        UserRepository userRepository = mock(UserRepository.class);
        service.userRepository = userRepository;
        User user = new User();
        Tournament tournament = new Tournament();
        user.setLastUsedTournament(tournament);
        TournamentClass tournamentClass = new TournamentClass();
        tournament.addClass(tournamentClass);

        when(userRepository.findByLoginName(anyString())).thenReturn(user);

        assertThat(service.getNotRunningOrStartPhase2("bla").size(), is(1));

        tournamentClass.setPhase(1);
        assertThat(service.getNotRunningOrStartPhase2("bla").size(), is(1));
        TournamentSingleGame game = new TournamentSingleGame();
        TournamentGroup group = new TournamentGroup();
        group.addGame(game);
        tournamentClass.addGroup(group);

        tournamentClass.setRunning(true);
        assertThat(service.getNotRunningOrStartPhase2("bla").size(), is(0));

        //all games were played
        game.setPlayed(true);
        assertThat(service.getNotRunningOrStartPhase2("bla").size(), is(1));

    }
}