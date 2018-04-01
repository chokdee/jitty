/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.config;

import com.jmelzer.jitty.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Long getActualTournamentId() {
        String name = getActualUsername();
        if (name != null) {
            return userService.findTournamentByLoginUser(name).getId();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public String getActualUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null &&
                context.getAuthentication().isAuthenticated() &&
                //when Anonymous Authentication is enabled
                !(context.getAuthentication() instanceof AnonymousAuthenticationToken)) {

            User user = (User) context.getAuthentication().getPrincipal();
            return user.getUsername();

        }
        return null;
    }
}
