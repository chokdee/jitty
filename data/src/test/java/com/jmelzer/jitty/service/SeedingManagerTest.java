package com.jmelzer.jitty.service;

import com.jmelzer.jitty.model.*;
import com.jmelzer.jitty.model.dto.TournamentGroupDTO;
import com.jmelzer.jitty.model.dto.TournamentPlayerDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;

/**
 * Created by J. Melzer on 31.07.2016.
 * test SeedingManager
 */
public class SeedingManagerTest {
    SeedingManager seedingManager = new SeedingManager();

    @Test
    public void assignPlayerToKoField() {
        KOField koField = new KOField();
        koField.setRound(new Round());
        koField.getRound().setGameSize(4);
        List<TournamentGroup> groups = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            TournamentGroup group = new TournamentGroup();
            groups.add(group);
            List<PlayerStatistic> ranking = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                PlayerStatistic ps = new PlayerStatistic();
                ps.player = new TournamentPlayer((long)i*j, "" + i + j, "" + i + j);
                ps.player.setQttr(1000 + i + j + i*j);
                group.addPlayer(ps.player);
                ranking.add(ps);
            }
            group.setRanking(ranking);
            System.out.println("----------------- ranking -----------");
            for (PlayerStatistic playerStatistic : ranking) {
                System.out.println(playerStatistic.player.getQttr());
            }
            System.out.println("----------------- ranking -----------");
        }
        List<TournamentSingleGame> games = seedingManager.assignPlayerToKoField(koField, groups);
        for (TournamentSingleGame game : games) {
            System.out.println("game = " + game);
        }
    }
    @Test
    public void setPlayerRandomAccordingToQTTR() {
        Random random = new Random();
        List<TournamentGroupDTO> allGroups = new ArrayList<>();
        List<TournamentPlayerDTO> allPlayer = new ArrayList<>();
        for (int n = 1; n <= 10; n++) {

            allGroups.add(new TournamentGroupDTO());
            int end = random.nextInt(10);
            allPlayer.clear();
            for (int p = 0; p < n * 6; p++) {
                allPlayer.add(new TournamentPlayerDTO());
                if (allPlayer.size() >= allGroups.size()) {
                    List<TournamentPlayerDTO> player = new ArrayList<>(allPlayer);
                    reset(allGroups);
                    int maxPerGroup = (int) Math.ceil((float) allPlayer.size() / (float) allGroups.size());
                    int minPerGroup = (int) Math.floor((float) allPlayer.size() / (float) allGroups.size());
                    seedingManager.setPlayerRandomAccordingToQTTR(allGroups, player);
                    for (TournamentGroupDTO group : allGroups) {
                        assertThat(group.getPlayers().size(), is(lessThan(maxPerGroup + 1)));
                        assertThat(group.getPlayers().size(), is(greaterThanOrEqualTo(minPerGroup)));
                    }
                }
            }

        }


    }

    private void reset(List<TournamentGroupDTO> allGroups) {
        for (TournamentGroupDTO group : allGroups) {
            group.getPlayers().clear();
        }
    }
}
