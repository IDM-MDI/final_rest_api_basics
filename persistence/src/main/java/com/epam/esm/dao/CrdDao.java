package com.epam.esm.dao;

import com.epam.esm.exception.DaoException;

import java.util.List;

public interface CrdDao<T,K> {
    long create(T entity) throws DaoException;
    List<T> read() throws DaoException;
    void delete(K id) throws DaoException;
}
