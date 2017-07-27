/*
 * Copyright (c) 2017.
 * J. Melzer
 */


package com.jmelzer.jitty.dao;


import com.jmelzer.jitty.model.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepository extends JpaRepository<Association, Long> {
    Association findByShortNameIgnoreCase(String name);
}
