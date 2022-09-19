package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.links.TagLinks.*;

@Component
public class TagHateoas extends HateoasDTO<TagDto> {

    @Override
    public void addLinks(TagDto dto) throws ServiceException, RepositoryException {
        getByLink(dto);
        addNewLink(dto);
        deleteLink(dto);
    }

    @Override
    protected void addPageLink(DtoPage<TagDto> dtoPage, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException {
        if (dtoPage.getType() == ControllerType.TAG_ALL) {
            getAllTag(dtoPage, number, size, sort, direction, rel);
        }
        else if (dtoPage.getType() == ControllerType.TAG_TOP) {
            getTopTags(dtoPage, number, size, rel);
        }
    }
}
