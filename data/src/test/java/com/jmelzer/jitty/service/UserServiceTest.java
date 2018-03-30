/*
 * Copyright (c) 2018.
 * J. Melzer
 */

package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.User;
import com.jmelzer.jitty.model.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by J. Melzer on 30.03.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    User user1 = new User("1n", "1p", "1e");

    User user2 = new User("2n", "2p", "2e");

    @InjectMocks
    UserService service = new UserService();

    @Mock
    UserRepository repository;

    @Mock
    TournamentRepository tournamentRepository;

    @Test
    public void findAll() {
        when(repository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserDTO> result = service.findAll();
        assertEquals(2, result.size());
        assertEquals("1n", result.get(0).getName());
        assertEquals("2n", result.get(1).getName());
    }

    @Test
    public void getOne() {
        when(repository.getOne(1L)).thenReturn(user1);

        UserDTO result = service.getOne(1L);
        assertEquals("1n", result.getName());

        when(repository.getOne(2L)).thenThrow(new EntityNotFoundException());
        try {
            service.getOne(2L);
            fail();
        } catch (EntityNotFoundException e) {
        }
    }

    @Test
    public void delete() {
        service.delete(1L);
        verify(repository).deleteById(1L);

    }

    @Test
    public void save() {
        UserDTO user = new UserDTO(1L);
        when(repository.getOne(1L)).thenReturn(user1);
        service.save(user);
        verify(repository).save(user1);
    }

    @Test
    public void findTournamentByLoginUser() {
        Tournament t = new Tournament();
        user2.setLastUsedTournament(t);
        when(repository.findByLoginName("1e")).thenReturn(user2);
        assertSame(t, service.findTournamentByLoginUser("1e"));
    }

    @Test
    public void findByLoginName() {
        when(repository.findByLoginName("1p")).thenReturn(user1);
        UserDTO user = service.findByLoginName("1p");

        assertEquals(user1.getName(), user.getName());
    }

    @Test
    public void changePassword() {
        when(repository.getOne(1L)).thenReturn(user1);
        service.changePassword(1L, "xxx");
        verify(repository).saveAndFlush(user1);
        assertEquals("xxx", user1.getPassword());
    }

    @Test
    public void selectTournamentForUser() {
        when(repository.getOne(1L)).thenReturn(user1);
        Tournament t = new Tournament();
        when(tournamentRepository.getOne(10L)).thenReturn(t);
        service.selectTournamentForUser(1L, "10");
        verify(repository).saveAndFlush(user1);
        assertSame(t, user1.getLastUsedTournament());
    }
}