package com.epam.esm.service;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.exception.RepositoryException;

public interface PageService<P extends DtoPage<D>,D> {
    P save(D dto) throws RepositoryException;
    P update(D dto, long id) throws RepositoryException;
    P delete(long id) throws RepositoryException;
    P findByPage(int page, int size, String sort) throws RepositoryException;
    P findById(long id) throws RepositoryException;
    P findByParam(D dto) throws RepositoryException;
}
