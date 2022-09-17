package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.links.UserLinks.*;

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
        if(dto.getOrders() != null) {
            for (OrderDto order : dto.getOrders()) {
                giftHateoas.addLinks(order.getGift());
            }
        }
    }

    @Override
    protected void addPageLink(DtoPage<UserDto> dtoPage, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException {
        switch (dtoPage.getType()) {
            case USER_ALL -> getAllUser(dtoPage, number, size, sort, direction, rel);
            case USER_BY_TOP -> getTopUsers(dtoPage,number, size, sort, direction, rel);
        }
    }
}
