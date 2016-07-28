package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.TournamentRepository;
import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.Tournament;
import com.jmelzer.jitty.model.User;
import com.jmelzer.jitty.model.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 15.07.2016.
 */
@Component("userService")
public class UserService {
    @Resource
    UserRepository repository;

    @Resource
    TournamentRepository tournamentRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> users = repository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(user, dto);
            userDTOs.add(dto);
        }
        return userDTOs;
    }

    @Transactional
    public UserDTO findOne(Long aLong) {
        User user = repository.findOne(aLong);
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    @Transactional
    public void delete(Long aLong) {
        repository.delete(aLong);
    }

    @Transactional
    public User save(UserDTO userDTO) {
        User user = repository.findOne(userDTO.getId());
        BeanUtils.copyProperties(userDTO, user);
        return repository.save(user);
    }

    @Transactional
    public UserDTO findByLoginName(String userName) {
        UserDTO userDTO = new UserDTO();
        User user = repository.findByLoginName(userName);
        BeanUtils.copyProperties(user, userDTO);
        if (user.getLastUsedTournament() != null) {
            userDTO.setLastUsedTournamentName(user.getLastUsedTournament().getName());
        }
        return userDTO;
    }

    @Transactional
    public void changePassword(Long id, String password) {
        User user = repository.findOne(id);
        user.setPassword(password);
        repository.saveAndFlush(user);
    }

    @Transactional
    public void selectTournamentForUser(User user, String id) {
        Tournament t = tournamentRepository.findOne(Long.valueOf(id));
        user.setLastUsedTournament(t);
        repository.saveAndFlush(user);

    }
}
