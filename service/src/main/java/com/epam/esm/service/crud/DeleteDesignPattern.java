package com.epam.esm.service.crud;

import com.epam.esm.exception.RepositoryException;

public interface DeleteDesignPattern {
    void delete(long id) throws RepositoryException;
}
