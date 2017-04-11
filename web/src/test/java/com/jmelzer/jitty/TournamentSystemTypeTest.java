/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmelzer.jitty.model.Distance;
import com.jmelzer.jitty.model.TournamentSystemType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by J. Melzer on 08.04.2017.
 */
public class TournamentSystemTypeTest {

    @Test
    public void convert() throws JsonProcessingException {
        System.out.println(new ObjectMapper().writeValueAsString(TournamentSystemType.GK));
        System.out.println(new ObjectMapper().writeValueAsString(Distance.MILE));
    }

}