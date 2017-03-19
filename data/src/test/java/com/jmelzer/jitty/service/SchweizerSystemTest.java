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
public class SchweizerSystemTest {
    SchweizerSystem schweizerSystem = new SchweizerSystem();

    @Test
    public void calcRankingFirstRound() throws Exception {
        List<TournamentPlayerDTO> player = createPlayer();
        schweizerSystem.calcRankingFirstRound(player);
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
//        player.add(new TournamentPlayerDTO("5", 999));
//        player.add(new TournamentPlayerDTO("6", 998));
//        player.add(new TournamentPlayerDTO("7", 997));
//        player.add(new TournamentPlayerDTO("8", 996));
//        player.add(new TournamentPlayerDTO("9", 995));
//        player.add(new TournamentPlayerDTO("10", 994));
//        player.add(new TournamentPlayerDTO("11", 993));
//        player.add(new TournamentPlayerDTO("12", 992));
        return player;
    }

    @Test
    public void calcRankingSecondRound() throws Exception {
        List<TournamentPlayerDTO> player = createPlayer();
        schweizerSystem.calcRankingFirstRound(player);
        List<TournamentSingleGameDTO> games = schweizerSystem.createGamesForTheFirstRound(player);

        for (TournamentSingleGameDTO game : games) {
            System.out.println("game = " + game);
            game.setWinner(2);
        }
        System.out.println("------ round 2 starts .-------------- ");
        schweizerSystem.calcRankingRound(2, player);

        for (TournamentPlayerDTO playerDTO : player) {
            System.out.println("playerDTO = " + playerDTO);
        }

        games = schweizerSystem.createGamesForRound(2, player);
        for (TournamentSingleGameDTO game : games) {
            System.out.println("game = " + game);
            game.setWinner(2);
        }

        System.out.println("------ round 3 starts .-------------- ");
        schweizerSystem.calcRankingRound(3, player);

        for (TournamentPlayerDTO playerDTO : player) {
            System.out.println("playerDTO = " + playerDTO);
        }
        games = createGames(player, 3);

        for (TournamentSingleGameDTO game : games) {
            System.out.println("game = " + game);
            game.setWinner(2);
        }

        System.out.println("------ round 4 starts .-------------- ");
        schweizerSystem.calcRankingRound(4, player);

        for (TournamentPlayerDTO playerDTO : player) {
            System.out.println("playerDTO = " + playerDTO);
        }

        games = createGames(player, 4);
        for (TournamentSingleGameDTO game : games) {
            System.out.println("game = " + game);
            game.setWinner(1);
        }
    }

    private List<TournamentSingleGameDTO> createGames(List<TournamentPlayerDTO> player, int round) {
        List<TournamentSingleGameDTO> games;
        games = null;
        for (int i = 1; i < 2; i++) {
            try {
                System.out.println("------ #" +  i + " run -------------- ");
                games = schweizerSystem.createGamesForRound(round, player);
                break;
            } catch (RuntimeException e) {
            }
        }
        return games;
    }

}