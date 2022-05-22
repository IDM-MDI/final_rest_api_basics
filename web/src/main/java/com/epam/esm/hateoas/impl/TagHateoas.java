package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;
import org.springframework.stereotype.Component;

import static com.epam.esm.controller.ControllerClass.TAG_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagHateoas implements HateoasDTO<TagDto> {

    @Override
    public void addLinks(TagDto dto) throws ServiceException, RepositoryException {
        getByLink(dto);
        addNewLink(dto);
        deleteLink(dto);
    }

    private void addNewLink(TagDto dto) {
        dto.add(linkTo(
                methodOn(TAG_CONTROLLER)
                        .addTag(dto))
                .withRel("add"));
    }
    private void deleteLink(TagDto dto) {
        dto.add(linkTo(
                methodOn(TAG_CONTROLLER)
                        .deleteTag(dto.getId()))
                        .withRel("delete"));
    }
    private void getByLink(TagDto dto) throws ServiceException, RepositoryException {
        dto.add(linkTo(
                methodOn(TAG_CONTROLLER)
                        .getByIdTag(dto.getId()))
                        .withSelfRel());
    }
}
