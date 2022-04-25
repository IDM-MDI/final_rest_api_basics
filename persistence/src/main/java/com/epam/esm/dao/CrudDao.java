package com.epam.esm.dao;


import com.epam.esm.exception.DaoException;

public interface CrudDao<T,K> extends CrdDao<T,K> {
    void update(T entity, K id) throws DaoException;
}
