/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import com.jmelzer.jitty.model.dto.TournamentSingleGameDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by J. Melzer on 17.03.2017.
 */
public class SwissSystemManagerTest {
    SwissSystemManager swissSystemManager = new SwissSystemManager();

    @Test
    public void calcRankingFirstRound() throws Exception {
        List<TournamentPlayerDTO> player = createPlayer();
        swissSystemManager.calcRankingFirstRound(player);
        assertEquals(1500, player.get(0).getQttr());
        assertEquals(1200, player.get(1).getQttr());
        assertEquals(1100, player.get(2).getQttr());
        assertEquals(1000, player.get(3).getQttr());
    }

    private List<TournamentPlayerDTO> createPlayer() {
        List<TournamentPlayerDTO> player = new ArrayList<>();
        player.add(new TournamentPlayerDTO("1", 1000));
        player.add(new TournamentPlayerDTO("2", 1200));
        player.add(new TournamentPlayerDTO("3", 1100));
        player.add(new TournamentPlayerDTO("4", 1500));
        player.add(new TournamentPlayerDTO("5", 999));
        player.add(new TournamentPlayerDTO("6", 998));
        player.add(new TournamentPlayerDTO("7", 997));
        player.add(new TournamentPlayerDTO("8", 996));
        player.add(new TournamentPlayerDTO("9", 995));
        player.add(new TournamentPlayerDTO("10", 994));
        player.add(new TournamentPlayerDTO("11", 993));
        player.add(new TournamentPlayerDTO("12", 992));
        return player;
    }

    @Test
    public void calcRankingSixRounds() throws Exception {
        for (int i = 0; i < 1000; i++) {

            System.out.println("HHHHHHHHHHHHHH   " + i + " HHHHHHHHHHHHHH");
            List<TournamentPlayerDTO> player = createPlayer();
            swissSystemManager.calcRankingFirstRound(player);
            System.out.println("------ round 1 starts .-------------- ");
            List<TournamentSingleGameDTO> games = swissSystemManager.createGamesForTheFirstRound(player);

            for (TournamentSingleGameDTO game : games) {
                System.out.println("game = " + game);
                game.setWinner(2);
            }
            for (int round = 2; round <= 6; round++) {

                System.out.println("------ round " + round + " starts .-------------- ");
                swissSystemManager.calcRankingRound(round, player);

                for (TournamentPlayerDTO playerDTO : player) {
                    System.out.println("playerDTO = " + playerDTO);
                }

                games = createGames(player, round);
                for (TournamentSingleGameDTO game : games) {
                    System.out.println("game = " + game);
                    game.setWinner(1);
                }
            }

            System.out.println(" ### RESULT ###");
            swissSystemManager.calcRankingRound(7, player);
            for (TournamentPlayerDTO playerDTO : player) {
                System.out.println("playerDTO = " + playerDTO);
            }
        }
    }

    private List<TournamentSingleGameDTO> createGames(List<TournamentPlayerDTO> player, int round) {
        List<TournamentSingleGameDTO> games;
        games = null;
        for (int i = 1; i <= 100; i++) {
            try {
                System.out.println("------ #" + i + " run -------------- ");
                if (i > 3) {
                    System.out.println("brute force !!");
                    games = swissSystemManager.createGamesForRound(round, player, true);
                } else {
                    games = swissSystemManager.createGamesForRound(round, player, false);
                }
                break;
            } catch (SwissRuntimeException e) {
                System.out.println();
            }
        }
        return games;
    }

}