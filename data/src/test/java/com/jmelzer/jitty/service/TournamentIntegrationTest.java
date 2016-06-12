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

import java.util.*;

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
        List<TournamentGroup> groups = tournamentService.caluculateGroups(classC);
        // test the order of the groups player
        for (TournamentGroup group : groups) {
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
        tournamentService.calcGroupGames(groups);

        //todo add table manager to get free tables

        //start the games, we assume we have 20 table to play
        //algo for find the player who can play
        Queue<TournamentSingleGame> gameQueue = new LinkedList<>();
        tournamentService.addPossibleGamesToQueue(groups, gameQueue, tournamentService.getBusyGames());
        int i = 0;
        for (TournamentSingleGame tournamentSingleGame : gameQueue) {
            System.out.println(++i + ": " + tournamentSingleGame);
        }
        //20 free tables
        TableManager tableManager = new TableManager();
        for (int j = 1; j < 21; j++) {
            tableManager.setFreeTablesNo(j);
        }

        callPossibleGames(tournamentService, gameQueue, tableManager);


        //list the busy games in a table on the UI
        i = 0;
        int max = 6 * groups.size();
        while (true) {

            List<TournamentSingleGame> busyGames = tournamentService.getBusyGames();
            if (busyGames.size() == 0) {
                break;
            }
            //get random busy game
            TournamentSingleGame game = busyGames.get(randomIntFromInterval(0, busyGames.size() - 1));

            //todo generate random results?
            game.addSet(new GameSet(11, 5));
            game.addSet(new GameSet(11, 9));
            game.addSet(new GameSet(12, 10));
            game.setWinner(1);
            tournamentService.removeBusyGame(game);


            System.out.println("game " + game + " finished with " + game.printResult());
            tournamentService.addPossibleGamesToQueue(groups, gameQueue, busyGames);
            tableManager.setFreeTablesNo(game.getTableNo());

            callPossibleGames(tournamentService, gameQueue, tableManager);
            System.out.println("possible game count = " + gameQueue.size());
            i++;
        }
        assertEquals(max, i);
    }

    private void callPossibleGames(TournamentService tournamentService, Queue<TournamentSingleGame> gameQueue, TableManager tableManager) {
        //let's play
        while (tableManager.isTableAvaible()) {
            TournamentSingleGame singleGame = gameQueue.poll();
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