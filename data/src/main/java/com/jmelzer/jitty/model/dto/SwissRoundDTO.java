/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 20.07.2017.
 */
public class SwissRoundDTO {
    public String name;

    List<SwissRoundResultDTO> results = new ArrayList<>();

    public void addResult(SwissRoundResultDTO rr) {
        results.add(rr);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SwissRoundResultDTO> getResults() {
        return results;
    }

    public void setResults(List<SwissRoundResultDTO> results) {
        this.results = results;
    }
}
