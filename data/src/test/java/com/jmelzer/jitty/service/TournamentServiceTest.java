/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.*;
import com.jmelzer.jitty.exceptions.IntegrityViolation;
import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.jmelzer.jitty.service.TournamentService.TABLE_COUNT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

/**
 * Created by J. Melzer on 11.06.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TournamentServiceTest {

    TournamentClass tc = new TournamentClass("testclass");

    @InjectMocks
    TournamentService service = new TournamentService();

    @Mock
    PlatformTransactionManager txManager;

    @Mock
    UserRepository userRepository;

    @Mock
    PhaseRepository phaseRepository;

    @Mock
    TournamentClassRepository tcRepository;

    @Mock
    TournamentRepository repository;

    @Mock
    TournamentSingleGameRepository tournamentSingleGameRepository;

    @Mock
    QueueManager queueManager;

    @Mock
    WorkflowManager workflowManager;

    @Mock
    TableManager tableManager;


    @Before
    public void before() {
        if (tc.getActivePhase() == null) {
            tc.createPhaseCombination(PhaseCombination.GK);
        }
    }

    @Test
    public void moveGameBackToPossiblegames() throws Exception {

        when(tournamentSingleGameRepository.getOne(1L)).thenReturn(null);
        try {
            service.moveGameBackToPossiblegames(1L);
            fail();
        } catch (IllegalArgumentException e) {
            //should be
        }

        TournamentSingleGame game = new TournamentSingleGame();
        when(tournamentSingleGameRepository.getOne(1L)).thenReturn(game);

        service.moveGameBackToPossiblegames(1L);

        verify(tableManager).pushFreeTable(game);
        verify(queueManager).moveGameBackToPossibleGames(game);
    }


    @Test
    public void testAddPossibleGamesToQueue() throws Exception {
        List<TournamentSingleGame> emptyList = new ArrayList<>();
        when(queueManager.getBusyGamesOriginal()).thenReturn(emptyList);
        GameQueue gameQueue = new GameQueue();
        when(queueManager.getQueueO()).thenReturn(gameQueue);
        when(tableManager.getFreeTableCount()).thenReturn(4);
        when(tableManager.pollFreeTableNo(any())).thenReturn(1);
        TournamentGroup group = prepareGroupWithPlayerAndGames();
        List<TournamentSingleGame> games = new ArrayList<>(group.getGames());
        gameQueue.setGames(emptyList);

        service.tournamentSingleGameRepository = tournamentSingleGameRepository;

        when(queueManager.getGamesFromQueue(4, 1L)).thenReturn(emptyList);

        List<TournamentGroup> groups = new ArrayList<>();
        groups.add(group);
        assertEquals(2, service.addPossibleGroupGamesToQueue(groups));

        when(queueManager.getGamesFromQueue(4, 1L)).thenReturn(games);
        when(tournamentSingleGameRepository.getOne(any())).thenReturn(new TournamentSingleGame("", 1L));
        service.startPossibleGames(1L);
        //must be saved
        verify(tournamentSingleGameRepository, atLeast(1)).save(isA(TournamentSingleGame.class));
//        verify(gameQueueRepository, atLeast(1)).saveAndFlush(queue);


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

    private void addGame(TournamentGroup group, int i, int j) {
        TournamentSingleGame game;
        game = new TournamentSingleGame();
        game.setId((long) i * j);
        game.setPlayer1(group.getPlayers().get(i));
        game.setPlayer2(group.getPlayers().get(j));
        group.addGame(game);
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
//        for (PlayerStatistic playerStatistic : group.getRanking()) {
//            System.out.println("playerStatistic = " + playerStatistic);
//        }
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
    public void getAllClassesWithStatus() {
        WorkflowManager workflowManager = new WorkflowManager();
        UserRepository userRepository = mock(UserRepository.class);
        service.userRepository = userRepository;
        User user = new User();
        Tournament tournament = new Tournament();
        user.setLastUsedTournament(tournament);
        TournamentClass tournamentClass = new TournamentClass();
        tournamentClass.createPhaseCombination(PhaseCombination.GK);
        tournament.addClass(tournamentClass);

        when(userRepository.findByLoginName(anyString())).thenReturn(user);

        assertThat(service.getAllClassesWithStatus("bla").size(), is(1));
        assertThat(service.getAllClassesWithStatus("bla").get(0).getStatus(), is(TournamentClassStatus.NOTSTARTED));

        tournamentClass.setActivePhaseNo(0);
        assertThat(service.getAllClassesWithStatus("bla").size(), is(1));
        TournamentSingleGame game = new TournamentSingleGame();
        TournamentGroup group = new TournamentGroup();
        group.addGame(game);
        tournamentClass.addGroup(group);

        tournamentClass.setRunning(true);
        tournamentClass.setActivePhaseNo(0);
        tournamentClass.setStatus(workflowManager.calcStatus(tournamentClass));
        assertThat(service.getAllClassesWithStatus("bla").size(), is(1));
        assertThat(service.getAllClassesWithStatus("bla").get(0).getStatus(), is(TournamentClassStatus.PHASE1_STARTED_NOT_CALLED));

        //all games were played
        game.setPlayed(true);
        tournamentClass.setStatus(workflowManager.calcStatus(tournamentClass));
        assertThat(service.getAllClassesWithStatus("bla").size(), is(1));
        assertThat(service.getAllClassesWithStatus("bla").get(0).getStatus(), is(TournamentClassStatus.PHASE1_AND_RESULTS));

        tournamentClass.setActivePhaseNo(1);
        KOField koField = new KOField();
        koField.setNoOfRounds(2);
        Round r1 = new Round();
        koField.setRound(r1);
        TournamentSingleGame rg1 = new TournamentSingleGame();
        r1.addAllGames(Collections.singletonList(rg1));
        koField.setRound(r1);
        TournamentSingleGame rg2 = new TournamentSingleGame();
        Round r2 = new Round();
        r1.setNextRound(r2);
        r2.addAllGames(Collections.singletonList(rg2));
        ((KOPhase) tournamentClass.getActivePhase()).setKoField(koField);
        tournamentClass.setStatus(workflowManager.calcStatus(tournamentClass));
        assertThat(service.getAllClassesWithStatus("bla").get(0).getStatus(), is(TournamentClassStatus.PHASE2_STARTED_NOT_CALLED));

        rg1.setPlayed(true);
        rg1.setWinner(1);
        tournamentClass.setStatus(workflowManager.calcStatus(tournamentClass));
        assertThat(service.getAllClassesWithStatus("bla").get(0).getStatus(), is(TournamentClassStatus.PHASE2_AND_RESULTS));

        rg2.setPlayed(true);
        rg2.setWinner(1);
        tournamentClass.setStatus(workflowManager.calcStatus(tournamentClass));
        assertThat(service.getAllClassesWithStatus("bla").get(0).getStatus(), is(TournamentClassStatus.FINISHED));
    }

    @Test
    public void saveAndFinishGame() {
        TournamentSingleGameDTO g1 = new TournamentSingleGameDTO();
        g1.setId(1L);
        g1.addSet(new GameSetDTO(11, 9));
        g1.setTcName("testi");
        TournamentSingleGame game = new TournamentSingleGame();
        game.setPlayer1(new TournamentPlayer());
        game.setPlayer2(new TournamentPlayer());
        game.setTid(9L);

        when(tournamentSingleGameRepository.getOne(1L)).thenReturn(game);

        TournamentClass tc = new TournamentClass();
        Tournament tournament = new Tournament();
        tournament.addClass(tc);
        tc.setName("testi");
        tc.setRunning(true);
        assertThat(tc.getStatus(), is(TournamentClassStatus.NOTSTARTED));

        when(repository.getOne(9L)).thenReturn(tournament);
        when(workflowManager.calcStatus(tc)).thenReturn(TournamentClassStatus.NOT_INITIALIZED);

        service.saveAndFinishGame(g1);

        assertThat(tc.getStatus(), is(TournamentClassStatus.NOT_INITIALIZED));
    }

    @Test
    public void addPossibleKoGamesToQueue() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void getOne() {
    }

    @Test
    public void create() {
        TournamentDTO dto = new TournamentDTO();
        Tournament t = new Tournament();
        t.setId(1L);
        t.setTableSettings(new TableSettings());
        t.getTableSettings().setTableCount(TABLE_COUNT);
        when(repository.saveAndFlush(any())).thenReturn(t);
        when(repository.getOne(1L)).thenReturn(t);
        assertSame(t, service.create(dto));
    }

    @Test
    public void selectTournament() {
    }

    @Test
    public void addPossibleGroupGamesToQueue() {
    }

    @Test
    public void playerInQueue() {
    }

    @Test
    public void update() {
    }

    @Test
    public void getOneClass() {
    }

    @Test
    public void deleteClass() {
    }

    @Test
    public void addTC() {
    }

    @Test
    public void getAllClasses() {
    }

    @Test
    public void getPlayerforClass() {
    }

    @Test
    public void createDummyPlayer() {
    }

    @Test
    public void randomIntFromInterval() {
    }

    @Test
    public void saveGame() {
    }

    @Test
    public void getFinishedGames() {
    }

    @Test
    public void loadGamesIntoQueue() {
        service.loadGamesIntoQueue();
    }

    @Test
    public void getStartedClasses() {
    }

    @Test
    public void getGroupResults() {
    }

    @Test
    public void markGroupWinner() {
    }

    @Test
    public void getGame() {
    }

    @Test
    public void areAllGroupsFinished() {
    }

    @Test
    public void isPhase1FinishedAndPhase2NotStarted() {
        TournamentClass tc = new TournamentClass();
        tc.createPhaseCombination(PhaseCombination.GK);
        assertFalse(service.isPhase1FinishedAndPhase2NotStarted(tc));

        TournamentGroup g = new TournamentGroup();
        TournamentSingleGame game = new TournamentSingleGame();
        game.setWinner(1);
        g.addGame(game);
        ((GroupPhase) tc.getSystem().getPhase(0)).getGroups().add(g);
        assertTrue(service.isPhase1FinishedAndPhase2NotStarted(tc));
    }

    @Test
    public void anyPhaseFinished() {
    }

    @Test
    public void save() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void getKOForClz() {
    }

    @Test
    public void saveTableCount() throws IntegrityViolation {

        Tournament t = new Tournament();
        t.setTableSettings(new TableSettings());
        t.getTableSettings().setTableCount(1);
        User user = new User();
        user.setLastUsedTournament(t);
        when(userRepository.findByLoginName("me")).thenReturn(user);

        service.saveTableCount("me", 11);
        assertEquals(11, (int) t.getTableSettings().getTableCount());

        verify(repository).saveAndFlush(t);
    }

    @Test
    public void startPossibleGames() {
    }

    @Test
    public void startGame() throws IntegrityViolation {
        TournamentSingleGame game = new TournamentSingleGame();
        when(tournamentSingleGameRepository.getOne(1L)).thenReturn(game);
        TournamentSingleGameDTO dto = service.startGame(1L);
        assertNotNull(dto);
    }

    @Test
    public void startGame1() throws IntegrityViolation {
        TournamentSingleGame game = new TournamentSingleGame();
        TournamentSingleGameDTO dto = service.startGame(game);
        assertNotNull(dto);
    }


    @Test
    public void updatePhase() {
        when(tcRepository.getOne(1L)).thenReturn(tc);
        tc.setActivePhaseNo(0);
        GroupPhase gp = new GroupPhase();
        gp.addGroup(new TournamentGroup());
        when(phaseRepository.getOne(1L)).thenReturn(gp);
        when(phaseRepository.saveAndFlush(gp)).thenReturn(gp);
        GroupPhaseDTO dto = new GroupPhaseDTO();
        dto.setId(1L);
        dto = service.updatePhase(1L, dto);
        assertNotNull(dto);
    }

    @Test
    public void updateClass() {
        when(tcRepository.getOne(any())).thenReturn(tc);
        assertNotNull(service.updateClass(new TournamentClassDTO()));
    }

    @Test
    public void actualPhase() {
    }

    @Test
    public void selectPhaseCombination() {
        service.selectPhaseCombination(1L, null);

        TournamentClass clz = new TournamentClass();
        when(tcRepository.getOne(1L)).thenReturn(clz);

        service.selectPhaseCombination(1L, PhaseCombination.GK);

        assertThat(clz.getActivePhaseNo(), is(-1));
        assertNull(clz.getActivePhase());

    }

    @Test
    public void hasOnlyOneClass() {
    }

    @Test
    public void exportToClickTT() {
    }

    @Test
    public void activatePhase() {
        TournamentClass clz = new TournamentClass();
        clz.createPhaseCombination(PhaseCombination.GK);
        when(tcRepository.getOne(1L)).thenReturn(clz);

        assertTrue(service.activatePhase(1L));

        verify(workflowManager, times(1)).calcStatus(clz);
        verify(tcRepository, times(1)).saveAndFlush(clz);

    }
}