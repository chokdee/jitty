/*
 * Copyright (c) 2016.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jmelzer.jitty.model.TournamentSystem;

/**
 * Created by J. Melzer on 28.12.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = KOPhaseDTO.class),
        @JsonSubTypes.Type(value = GroupPhaseDTO.class),
})
public abstract class PhaseDTO {

    private TournamentSystem system;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TournamentSystem getSystem() {
        return system;
    }

    public void setSystem(TournamentSystem system) {
        this.system = system;
    }
}
