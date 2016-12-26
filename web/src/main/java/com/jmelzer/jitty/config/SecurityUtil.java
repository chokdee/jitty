package com.jmelzer.jitty.config;

import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.Tournament;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by J. Melzer on 22.07.2016.
 */
@Component
public class SecurityUtil {

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
