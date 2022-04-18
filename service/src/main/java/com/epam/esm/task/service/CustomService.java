package com.epam.esm.task.service;

import com.epam.esm.task.exception.ServiceException;

import java.util.List;

public interface CustomService<T,K> {
    void save(T dto) throws ServiceException;
    void delete(K id) throws ServiceException;
    void update(T dto, K id) throws ServiceException;
    List<T> findAll() throws ServiceException;
    T findById(K id) throws ServiceException;
}
