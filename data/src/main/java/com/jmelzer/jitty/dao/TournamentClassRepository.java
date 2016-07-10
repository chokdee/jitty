/* 
* Copyright (C) allesklar.com AG
* All rights reserved.
*
* Author: juergi
* Date: 27.05.12 
*
*/


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.TournamentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentClassRepository extends JpaRepository<TournamentClass, Long> {
}
