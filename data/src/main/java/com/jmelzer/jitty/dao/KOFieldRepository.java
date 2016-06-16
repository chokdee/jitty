package com.jmelzer.jitty.dao;

import com.jmelzer.jitty.model.KOField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by J. Melzer on 15.06.2016.
 */
@Repository
public interface KOFieldRepository extends JpaRepository<KOField, Long> {
}
