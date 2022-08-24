package com.epam.esm.hateoas.links;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;

import static com.epam.esm.controller.ControllerClass.USER_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserLinks {
    public static void getAllUser(DtoPage<UserDto> dtoPage, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException {
        dtoPage.add(linkTo(methodOn(USER_CONTROLLER).
                getUsers(number, size, sort, direction)).
                withRel(rel));
    }

    public static void getByIdLink(UserDto dto) throws ServiceException, RepositoryException {
        dto.add(linkTo(
                methodOn(USER_CONTROLLER)
                        .getByIdUser(dto.getId()))
                .withSelfRel());
    }

    public static void getTopUsers(DtoPage<UserDto> dtoPage, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException {
        dtoPage.add(linkTo(
                methodOn(USER_CONTROLLER)
                        .getTopUsers())
                .withRel(rel));
    }
}
