/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmelzer.jitty.model.dto.SwissResultsDTO;
import com.jmelzer.jitty.model.dto.SwissRoundDTO;
import com.jmelzer.jitty.model.dto.SwissRoundResultDTO;
import org.junit.Test;

/**
 * Created by J. Melzer on 20.07.2017.
 */
public class JsonGenTest {

    @Test
    public void testGen1() throws JsonProcessingException {
        SwissResultsDTO result = new SwissResultsDTO();
        SwissRoundDTO round = new SwissRoundDTO();
        result.addRound(round);
        round.name = "111";
        SwissRoundResultDTO rr = new SwissRoundResultDTO();
        rr.name1 = "p1";
        rr.name2 = "p2";
        rr.sets = "11:0 11:0 11:0";
        rr.result = "3:0";
        round.addResult(rr);

        System.out.println(createJson(result));
    }

    String createJson(Object toBeConverted) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(toBeConverted);
    }
}
