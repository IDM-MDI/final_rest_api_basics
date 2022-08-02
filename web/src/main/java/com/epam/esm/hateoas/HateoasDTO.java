package com.epam.esm.hateoas;

import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;

public interface HateoasDTO<D> {
    void addLinks(D dto) throws ServiceException, RepositoryException;
}
