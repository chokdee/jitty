/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jmelzer.jitty.dao;

import com.jmelzer.jitty.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Integration tests for {@link UserRepository}.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TournamentRepositoryIntegrationTests {

    @Autowired
    TournamentRepository repository;
    @Autowired
    TournamentPlayerRepository playerRepository;

    @Test
    public void findsFirstPageOf() {

        List<Tournament> tournaments = repository.findAll();
        assertThat(tournaments.size(), is(greaterThan(0)));
    }

    @PostConstruct
    public void post() {

//        Thread t = new Thread() {
//            public void run() {
//                org.hsqldb.util.DatabaseManagerSwing.main(new String[]{
//                        "--url", "jdbc:hsqldb:mem:testdb", "--noexit"
//                });
//            }
//        };
//        t.start();

    }

    @Test
    @Transactional
    public void save() {
        Tournament tournament = new Tournament();
        tournament.setName("ABC Open");
        tournament.setStartDate(new Date());
        tournament.setEndDate(new Date());

        TournamentClass tournamentClass = new TournamentClass();
        tournamentClass.setName("A-Klasse bis 3000 TTR");
        tournament.addClass(tournamentClass);
        tournamentClass.createPhaseCombination(PhaseCombination.GK);
        TournamentClass tournamentClass2 = new TournamentClass();
        tournamentClass2.setName("B-Klasse bis 1800 TTR");
        tournamentClass2.createPhaseCombination(PhaseCombination.GK);
        tournament.addClass(tournamentClass2);


        repository.save(tournament);


        assertNotNull(tournament.getId());
        assertNotNull(tournamentClass.getId());
        assertNotNull(tournamentClass2.getId());

        Tournament tDB = repository.findOne(tournament.getId());

        List<TournamentPlayer> players = playerRepository.findAll();
        for (TournamentPlayer player : players) {
            tDB.getClasses().get(0).addPlayer(player);
            tDB.getClasses().get(1).addPlayer(player);
        }
        repository.save(tDB);

        tDB = repository.findOne(tournament.getId());

        assertEquals(players.size(), tDB.getClasses().get(0).getPlayers().size());
        assertEquals(players.size(), tDB.getClasses().get(1).getPlayers().size());


    }
}
