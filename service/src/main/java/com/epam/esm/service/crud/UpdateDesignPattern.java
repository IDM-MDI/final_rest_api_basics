package com.epam.esm.service.crud;

import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;

public interface UpdateDesignPattern<E,D> {
    E update(D dto) throws RepositoryException, ServiceException;
}
