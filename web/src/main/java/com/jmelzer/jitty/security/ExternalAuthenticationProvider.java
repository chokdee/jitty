/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.security;

import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * user password login
 */
@Component
public class ExternalAuthenticationProvider implements AuthenticationProvider {
    static final Logger logger = LoggerFactory.getLogger(ExternalAuthenticationProvider.class);

    @Autowired
    protected UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // send request to external service
        final String username = authentication.getPrincipal().toString();
        final String password = authentication.getCredentials().toString();

        User person = userRepository.findByLoginName(username);

        if (person == null) {
            throw new BadCredentialsException("Username not found.");
        }

        if (!password.equals(person.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }
        logger.info("user {} logged in", username);
        // we have to store password
        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(username, password, true,
                true, true, true, Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(principal, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
