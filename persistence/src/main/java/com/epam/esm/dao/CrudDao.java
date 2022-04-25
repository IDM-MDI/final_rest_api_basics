package com.epam.esm.task.dao;

import com.epam.esm.task.exception.DaoException;

public interface CrudDao<T,K> extends CrdDao<T,K>{
    void update(T entity, K id) throws DaoException;
}
