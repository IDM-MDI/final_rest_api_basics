package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.links.UserLinks.getAllUser;
import static com.epam.esm.hateoas.links.UserLinks.getByIdLink;
import static com.epam.esm.hateoas.links.UserLinks.getTopUsers;

@Component
public class UserHateoas extends HateoasDTO<UserDto> {
    private final GiftCertificateHateoas giftHateoas;

    @Autowired
    public UserHateoas(GiftCertificateHateoas giftHateoas) {
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

    @Override
    protected void addPageLink(DtoPage<UserDto> dtoPage, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException {
        switch (dtoPage.getType()) {
            case USER_ALL -> getAllUser(dtoPage, number, size, sort, direction, rel);
            case USER_BY_TOP -> getTopUsers(dtoPage,number, size, sort, direction, rel);
        }
    }
}
