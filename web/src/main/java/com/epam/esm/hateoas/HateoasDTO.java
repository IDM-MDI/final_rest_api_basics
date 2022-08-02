package com.epam.esm.hateoas;

import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;

public interface HateoasDTO<Dto> {
    void addLinks(Dto dto) throws ServiceException, RepositoryException;
}
