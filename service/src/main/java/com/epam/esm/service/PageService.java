package com.epam.esm.service;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;

public interface PageService<P extends DtoPage<D>,D> {
    P save(D dto) throws RepositoryException;
    P update(D dto, long id) throws RepositoryException, ServiceException;
    P delete(long id) throws RepositoryException;
    P findByPage(int page, int size, String sort, String direction) throws RepositoryException;
    P findById(long id) throws RepositoryException;
    P findByParam(D dto) throws RepositoryException;
    P findByActiveStatus(int page, int size, String sort, String direction) throws RepositoryException;
    P findByStatus(int page, int size, String sort, String direction,String statusName) throws RepositoryException;
}
