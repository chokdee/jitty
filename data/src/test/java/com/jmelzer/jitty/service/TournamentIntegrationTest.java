package com.jmelzer.jitty.service;

import com.jmelzer.jitty.SampleDataJpaApplication;
import com.jmelzer.jitty.TableManager;
import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by J. Melzer on 01.06.2016.
 * Try to figure out how to make a tournament.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SampleDataJpaApplication.class)
public class TournamentIntegrationTest {

    @Autowired
    TournamentPlayerRepository playerRepository;

    @Transactional
    @Test
    public void testit() {
        List<TournamentPlayer> allPlayer = new ArrayList<>();
        //ok lets start to have 59 player
        for (int i = 1; i <= 59; i++) {
            TournamentPlayer player = new TournamentPlayer();
            player.setId((long) i);
            player.setFirstName("Vorname#" + i);
            player.setLastName("Nachname#" + i);
            player.setTtr(randomIntFromInterval(1400, 1600));
            int qttr = randomIntFromInterval(player.getTtr() - 20, player.getTtr() + 20);
            player.setQttr(qttr);
            System.out.println("player = " + player);
            allPlayer.add(player);
        }


        //let's create the tournament
        Tournament tournament = new Tournament();
        tournament.setName("Jitty ITTF World Open");

        //create the classes for the tournament
        TournamentClass classC = new TournamentClass("C Klasse bis 1600");
        tournament.addClass(classC);

        allPlayer.forEach(classC::addPlayer);

        TournamentClass classB = new TournamentClass("B Klasse bis 1800");
        tournament.addClass(classB);

        //etc ...

        TournamentService tournamentService = new TournamentService();
        //start with the groups

        //do auslosung

        //calculate groups
        tournamentService.caluculateGroups(classC);
        assertEquals("14*4 groups + 1 with 3 player", 15, classC.getGroups().size());

        // test the order of the groups player
        for (TournamentGroup group : tournamentService.getGroups()) {
            int last_ttr = 5000;
            for (TournamentPlayer player : group.getPlayers()) {
                assertTrue(player.toString(), last_ttr > player.getQttr());
                last_ttr = player.getQttr();
            }
        }

//        for (TournamentGroup group : groups) {
//            System.out.println(group);
//        }
        //questions:
        //how many player in every group --> Configurable

        //start the tournament with games
        //calculate possible games
        tournamentService.calcGroupGames();

        //todo add table manager to get free tables

        //start the games, we assume we have 20 table to play
        //algo for find the player who can play

        tournamentService.addPossibleGroupGamesToQueue();
//        int i = 0;
//        for (TournamentSingleGame tournamentSingleGame : gameQueue) {
//            System.out.println(++i + ": " + tournamentSingleGame);
//        }
        //20 free tables
        TableManager tableManager = new TableManager();
        for (int j = 1; j < 21; j++) {
            tableManager.setFreeTablesNo(j);
        }

        callPossibleGames(tournamentService, tableManager);


        //list the busy games in a table on the UI
        int i = 0;
        int max = 6 * tournamentService.getGroups().size();
        while (true) {

            List<TournamentSingleGame> busyGames = tournamentService.getBusyGames();
            if (busyGames.size() == 0) {
                break;
            }
            //get random busy game
            TournamentSingleGame game = busyGames.get(randomIntFromInterval(0, busyGames.size() - 1));

            createRandomResult(game);

            tournamentService.removeBusyGame(game);

            System.out.println("game " + game + " finished with " + game.printResult());
            tournamentService.addPossibleGroupGamesToQueue();
            tableManager.setFreeTablesNo(game.getTableNo());

            callPossibleGames(tournamentService, tableManager);
            i++;
        }
        assertEquals(max, i);

        //todo remove bye games
        for (TournamentPlayer player : allPlayer) {
            assertEquals(player.toString(), 3, player.getGames().size());
        }

        tournamentService.markGroupWinner();

        //59 Player , 2 per group are winner 32 K.O
        KOField field = tournamentService.createKOField(RoundType.R32);
        System.out.println("field = " + field);
        List<TournamentSingleGame> games = tournamentService.assignPlayerToKoField(field);
        printBracket(games);

        tournamentService.addPossibleKoGamesToQueue();

        callPossibleGames(tournamentService, tableManager);

        for (TournamentSingleGame game : games) {
            createRandomResult(game);
            tournamentService.enterResult(game);

        }
        assertEquals(0, tournamentService.getQueueSize());


        tournamentService.addPossibleKoGamesToQueue();
        assertEquals(0, tournamentService.getQueueSize());

    }

    private void printBracket(List<TournamentSingleGame> games) {
        for (TournamentSingleGame game : games) {
            System.out.println("------------------");
            System.out.println(game.getPlayer1().getFullName());
            System.out.println("                       --------------");
            System.out.println(game.getPlayer2().getFullName());
            System.out.println("------------------");
            System.out.println();
        }
    }

    private void createRandomResult(TournamentSingleGame game) {
        int playedSets = randomIntFromInterval(3, 5);
        int winner = randomIntFromInterval(1, 2);
        int loser = 0;
        if (winner == 1) {
            loser = 2;
        } else {
            loser = 1;
        }
        game.setWinner(winner);
        for (int i = 1; i <= playedSets; i++) {
            if (i < 3) {
                addSetFor(winner, game);
            } else {
                if (playedSets == i) {
                    addSetFor(winner, game);
                } else {
                    addSetFor(loser, game);
                }
            }
        }
        assertTrue(playedSets < 6 && playedSets > 2);
        assertEquals(playedSets, game.getSets().size());
    }

    private void addSetFor(int winner, TournamentSingleGame game) {
        if (winner == 1) {
            game.addSet(new GameSet(11, randomIntFromInterval(0, 9)));
        } else {
            game.addSet(new GameSet(randomIntFromInterval(0, 9), 11));
        }

    }

    private void callPossibleGames(TournamentService tournamentService, TableManager tableManager) {
        //let's play
        while (tableManager.isTableAvaible()) {
            TournamentSingleGame singleGame = tournamentService.poll();
            if (singleGame == null) {
                break;
            }
            int no = tableManager.getFreeTableNo();
            System.out.println("---------------------------------");
            System.out.println("Aufruf an Tisch " + no);
            System.out.println(singleGame.getPlayer1().getFullName());
            System.out.println("gegen");
            System.out.println(singleGame.getPlayer2().getFullName());
            System.out.println("---------------------------------");
            singleGame.setCalled(true);
            singleGame.setStartTime(new Date());
            singleGame.setTableNo(no);
            tournamentService.addBusyGame(singleGame);
        }
    }


    int randomIntFromInterval(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}