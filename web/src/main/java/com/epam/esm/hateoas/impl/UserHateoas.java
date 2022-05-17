package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;

import static com.epam.esm.controller.ControllerClass.USER_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserHateoas implements HateoasDTO<UserDto> {
    @Override
    public void addLinks(UserDto dto) {
        try {
            getByIdLink(dto);
            addNewLink(dto);
            updateLink(dto);
            deleteLink(dto);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateLink(UserDto dto) throws ServiceException {
        dto.add(linkTo(
                methodOn(USER_CONTROLLER)
                        .updateUser(dto.getId(), dto))
                        .withRel("update"));
    }
    private void addNewLink(UserDto dto) throws ServiceException {
        dto.add(linkTo(
                methodOn(USER_CONTROLLER)
                        .addUser(dto))
                        .withRel("add"));
    }
    private void deleteLink(UserDto dto) throws ServiceException {
        dto.add(linkTo(
                methodOn(USER_CONTROLLER)
                        .deleteUser(dto.getId()))
                        .withRel("delete"));
    }
    private void getByIdLink(UserDto dto) throws ServiceException {
        dto.add(linkTo(
                methodOn(USER_CONTROLLER)
                        .getByIdUser(dto.getId()))
                        .withSelfRel());
    }
}
