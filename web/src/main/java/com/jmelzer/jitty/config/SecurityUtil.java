/*
 * Copyright (c) 2017.
 * J. Melzer
 */

package com.jmelzer.jitty.config;

import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by J. Melzer on 22.07.2016.
 */

@Component
public class SecurityUtil {

    @Resource
    UserService userService;

    @Transactional(readOnly = true)
    public Long getActualTournamentId() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null &&
                context.getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {

            String user = (String) context.getAuthentication().getPrincipal();
            return userService.findTournamentByLoginUser(user).getId();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Tournament getActualTournament() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null &&
                context.getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {

            String user = (String) context.getAuthentication().getPrincipal();
            return userService.findTournamentByLoginUser(user);
        }
        return null;
    }
    public String getActualUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null &&
                context.getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {

            return (String) context.getAuthentication().getPrincipal();

        }
        return null;
    }
}
