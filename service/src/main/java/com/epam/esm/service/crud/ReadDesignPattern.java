package com.epam.esm.service.crud;

import com.epam.esm.exception.RepositoryException;

import java.util.List;

public interface ReadDesignPattern<E,D> {
    List<E> findAll(int page, int size, String sort, String direction) throws RepositoryException;
    E findById(long id) throws RepositoryException;
    List<E> findByParam(D dto) throws RepositoryException;
    List<E> findByStatus(int page, int size, String sort, String direction, String statusName) throws RepositoryException;
}
