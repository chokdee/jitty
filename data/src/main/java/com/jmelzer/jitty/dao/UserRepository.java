/* 
* Copyright (C) allesklar.com AG
* All rights reserved.
*
* Author: juergi
* Date: 27.05.12 
*
*/


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {
    Page<User> findAll(Pageable pageable);

//    User findByUserNameNonLocked(String username);
//
//    User findByName(String username);
//
//    User findByEmail(String email);
}
