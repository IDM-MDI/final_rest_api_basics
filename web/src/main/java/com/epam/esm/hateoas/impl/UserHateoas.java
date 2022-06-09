package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.esm.controller.ControllerClass.USER_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoas implements HateoasDTO<UserDto> {

    private final PageHateoas<UserDto> pageHateoas;
    private final GiftCertificateHateoas giftHateoas;

    @Autowired
    public UserHateoas(PageHateoas<UserDto> pageHateoas, GiftCertificateHateoas giftHateoas) {
        this.pageHateoas = pageHateoas;
        this.giftHateoas = giftHateoas;
    }

    @Override
    public void addLinks(UserDto dto) throws ServiceException, RepositoryException {
        getByIdLink(dto);
        dto.getOrders().forEach(i-> {
            try {
                giftHateoas.addLinks(i.getGift());
            } catch (ServiceException | RepositoryException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void getByIdLink(UserDto dto) throws ServiceException, RepositoryException {
        dto.add(linkTo(
                methodOn(USER_CONTROLLER)
                        .getByIdUser(dto.getId()))
                        .withSelfRel());
    }

    public void setUserHateoas(DtoPage<UserDto> dtoPage) throws ServiceException, RepositoryException {
        for (UserDto dto : dtoPage.getContent()) {
            addLinks(dto);
        }
        if(dtoPage.getSize() == 0) {
            pageHateoas.addUserGetBackPage(dtoPage);
        }
        else {
            pageHateoas.addUsersPage(dtoPage);
        }
    }
}
