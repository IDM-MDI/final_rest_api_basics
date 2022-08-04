package com.epam.esm.service.crud;

import com.epam.esm.exception.RepositoryException;

public interface CreatDesignPattern<E,D>  {
    E save(D dto) throws RepositoryException;
}
