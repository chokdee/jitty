/*
 * Copyright (c) 2017.
 * J. Melzer
 */


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.Club;
import com.jmelzer.jitty.model.GameQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameQueueRepository extends JpaRepository<GameQueue, Long> {
}
