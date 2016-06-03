package com.jmelzer.jitty.service;

import com.jmelzer.jitty.SampleDataJpaApplication;
import com.jmelzer.jitty.dao.TournamentPlayerRepository;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.TournamentGroup;
import com.jmelzer.jitty.model.TournamentPlayer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

        //start with the groups

        //do auslosung

        //calculate groups
        List<TournamentGroup> groups = caluculateGroups(classC);
        for (TournamentGroup group : groups) {
            System.out.println(group);
        }
        //questions:
        //how many groups?
        //how many player in every group

        //
    }

    private List<TournamentGroup> caluculateGroups(TournamentClass tournamentClass) {
        //first calc the field size , 16 / 32 etc
        //todo smallest possible size calucation
        int ps = tournamentClass.getPlayers().size();
        int fieldSize = 0;
        for (int i = 4; i < 8; i++) {
            fieldSize = (int) Math.pow(2, i);
            if (fieldSize > ps) {
                System.out.println("found field size " + (int) Math.pow(2, i) + " for player size " + ps);
                break;
            }
        }


        int groupCount = calcOptimalGroupSize(ps);

        List<TournamentGroup> groups = createGroups(groupCount);

        //todo don't forget the rules see auslosung.txt
        List<TournamentPlayer> allPlayer = new ArrayList<>(tournamentClass.getPlayers());
        Random randomGenerator = new Random();

        //sort all player by qttr
        Collections.sort(allPlayer, (o1, o2) -> {
            if (o1.getQttr() < o2.getQttr()) return 1;
            if (o1.getQttr() > o2.getQttr()) return -1;
            return 0;
        });

        setPlayerRandomAccordingToQTTR(groupCount, groups, allPlayer, randomGenerator);

        // test the order of the groups player
        for (TournamentGroup group : groups) {
            int last_ttr = 5000;
            for (TournamentPlayer player : group.getPlayers()) {
                assertTrue(player.toString(), last_ttr > player.getQttr());
                last_ttr = player.getQttr();
            }
        }

        return groups;
    }

    private void setPlayerRandomAccordingToQTTR(int groupCount, List<TournamentGroup> groups, List<TournamentPlayer> allPlayer, Random randomGenerator) {
        while (allPlayer.size() > 0) {
            //set the best n player in the groups
            for (int i = 0; i < groupCount; i++) {
                int index = randomGenerator.nextInt(groupCount - i);
                if (index >= allPlayer.size()) {
                    index--;
                }
                if (index == -1 || allPlayer.size()==0) break;
                TournamentPlayer p = allPlayer.get(index);
                allPlayer.remove(index);
                groups.get(i).addPlayer(p);

            }
        }
    }

    private int calcOptimalGroupSize(int playerSize) {
        //how many groups?
        int optGroupSize = 4; //todo config it or let the user do manual move players
        int groupCount = playerSize / optGroupSize;
        int rest = (playerSize % optGroupSize);

        if (rest > 0)
            groupCount++;

        return groupCount;
    }

    private List<TournamentGroup> createGroups(int groupCount) {
        List<TournamentGroup> groups = new ArrayList<>(groupCount);
        char name = 'A';
        for (int i = 0; i < groupCount; i++) {
            TournamentGroup group = new TournamentGroup("" + name);
            groups.add(group);
            name++;
        }
        return groups;
    }

    int randomIntFromInterval(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}