package com.epam.esm.hateoas.links;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;

import static com.epam.esm.controller.ControllerClass.TAG_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class TagLinks {

    public static void addNewLink(TagDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(TAG_CONTROLLER)
                        .addTag(dto))
                .withRel("add"));
    }
    public static void deleteLink(TagDto dto) throws RepositoryException, ServiceException {
        dto.add(linkTo(
                methodOn(TAG_CONTROLLER)
                        .deleteTag(dto.getId()))
                .withRel("delete"));
    }
    public static void getByLink(TagDto dto) throws ServiceException, RepositoryException {
        dto.add(linkTo(
                methodOn(TAG_CONTROLLER)
                        .getByIdTag(dto.getId()))
                .withSelfRel());
    }

    public static void getAllTag(DtoPage<TagDto> dtoPage, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException {
        dtoPage.add(linkTo(methodOn(TAG_CONTROLLER).
                getTags(number, size, sort, direction)).
                withRel(rel));
    }

    public static void getTopTags(DtoPage<TagDto> dtoPage, int number, int size, String rel) throws ServiceException, RepositoryException {
        dtoPage.add(linkTo(methodOn(TAG_CONTROLLER).
                getTopTags(number, size)).
                withRel(rel));
    }
}
