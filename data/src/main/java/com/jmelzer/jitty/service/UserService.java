package com.jmelzer.jitty.service;

import com.jmelzer.jitty.dao.UserRepository;
import com.jmelzer.jitty.model.TournamentClass;
import com.jmelzer.jitty.model.User;
import com.jmelzer.jitty.model.dto.UserDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J. Melzer on 15.07.2016.
 */
@Component
public class UserService {
    @Resource
    UserRepository repository;

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> users = repository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(user, dto);
            userDTOs.add(dto);
        }
//        for (User user : users) {
//            if (user.getLastUsedTournament() != null) {
//                for (TournamentClass tournamentClass : user.getLastUsedTournament().getClasses()) {
//                    Hibernate.initialize(tournamentClass);
//                    Hibernate.initialize(tournamentClass.getGroups());
//                }
//            }
//        }
        return userDTOs;
    }

    @Transactional
    public User findOne(Long aLong) {
        return repository.findOne(aLong);
    }

    @Transactional
    public void delete(Long aLong) {
        repository.delete(aLong);
    }

    @Transactional
    public User save(User user) {
        return repository.save(user);
    }
}
