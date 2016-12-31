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

import com.jmelzer.jitty.model.KOField;
import com.jmelzer.jitty.model.Round;
import com.jmelzer.jitty.model.TournamentSingleGame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

/**
 * Integration tests for {@link UserRepository}.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class KOFieldRepositoryIntegrationTests {

    @Autowired
    KOFieldRepository repository;

    @Transactional
    @Test
    public void testSave() {
        KOField field = new KOField();
        //lets say 32 field
        Round round1 = new Round();
        field.setRound(round1);
        for (int i = 0; i < 16; i++) {
            round1.addGame(new TournamentSingleGame("A", 1L));
        }
        Round round2 = new Round();
        round1.setNextRound(round2);

        repository.saveAndFlush(field);
        assertNotNull(field.getId());

        assertNotNull(field.getRound().getId());
        assertNotNull(field.getRound().getNextRound().getId());
    }
}
