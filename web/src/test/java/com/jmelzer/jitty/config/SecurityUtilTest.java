/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

package com.jmelzer.jitty.config;

import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by J. Melzer on 01.04.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityUtilTest {

    @Resource
    SecurityUtil securityUtil;


    @Test
    @WithMockUser(value = "rob", roles = "ADMIN")
    public void getActualTournamentId() {
        UserService userService = mock(UserService.class);
        securityUtil.setUserService(userService);
        Tournament t = new Tournament();
        t.setId(1L);
        when(userService.findTournamentByLoginUser(any())).thenReturn(t);

        assertEquals(1L, (long) securityUtil.getActualTournamentId());
    }

    @Test
    public void getActualTournament() {
    }

    @Test
    public void getActualUsername() {
    }
}