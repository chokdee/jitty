/* 
* Copyright (C) allesklar.com AG
* All rights reserved.
*
* Author: juergi
* Date: 27.05.12 
*
*/


package com.jmelzer.jitty.dao;

import java.io.Serializable;
import java.util.List;

public interface AbstractDao<T extends Serializable> {
    T findOne(Long id);

    List<T> findAll();

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    void deleteById(Long entityId);

    T findOneByExample(T ex);

    List<String> findAllForAutoCompletion();
}
