package com.epam.esm.service;

import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface EntityService<T,K> {
    T save(K dto) throws RepositoryException;
    T update(T entity) throws RepositoryException;
    List<T> findAll(int page, int size, String sort) throws RepositoryException;
    T findById(long id) throws RepositoryException;
    List<T> findActive();
    List<T> findDeleted();
    void delete(long id);

    List<T> findByStatus(String statusName);
}
