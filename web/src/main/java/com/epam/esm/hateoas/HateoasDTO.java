package com.epam.esm.hateoas;

import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.WebException;

public interface HateoasDTO<DTO> {
    void addLinks(DTO dto) throws ServiceException, RepositoryException;
}
