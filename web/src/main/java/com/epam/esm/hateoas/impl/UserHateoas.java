package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.WebException;
import com.epam.esm.hateoas.HateoasDTO;
import org.springframework.stereotype.Component;

import static com.epam.esm.controller.ControllerClass.USER_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoas implements HateoasDTO<UserDto> {
    @Override
    public void addLinks(UserDto dto) throws ServiceException, RepositoryException {
        getByIdLink(dto);
    }

    private void getByIdLink(UserDto dto) throws ServiceException, RepositoryException {
        dto.add(linkTo(
                methodOn(USER_CONTROLLER)
                        .getByIdUser(dto.getId()))
                        .withSelfRel());
    }
}
