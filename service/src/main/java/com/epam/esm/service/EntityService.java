package com.epam.esm.service;

import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface EntityService<T,K> {
    T save(K dto) throws RepositoryException;
    T update(K dto) throws RepositoryException;
    List<T> findAll(int page, int size, String sort) throws RepositoryException;
    T findById(long id) throws RepositoryException;
    List<T> findByParam(K dto) throws RepositoryException;
    List<T> findActive() throws RepositoryException;
    List<T> findDeleted() throws RepositoryException;
    void delete(long id) throws RepositoryException;

    List<T> findByStatus(String statusName) throws RepositoryException;
}
