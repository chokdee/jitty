/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.model.dto;

/**
 * Created by J. Melzer on 16.07.2016.
 */
public class UserDTO {
    private Long id;

    private String loginName;

    private String name;

    private String email;

    private String lastUsedTournamentName;

    private long lastUsedTournamentId;

    private String password;

    public UserDTO() {
    }

    public UserDTO(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastUsedTournamentName() {
        return lastUsedTournamentName;
    }

    public void setLastUsedTournamentName(String lastUsedTournamentName) {
        this.lastUsedTournamentName = lastUsedTournamentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getLastUsedTournamentId() {
        return lastUsedTournamentId;
    }

    public void setLastUsedTournamentId(long lastUsedTournamentId) {
        this.lastUsedTournamentId = lastUsedTournamentId;
    }
}
