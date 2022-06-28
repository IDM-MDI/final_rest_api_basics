package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderHateoas implements HateoasDTO<OrderDto> {
    private final GiftCertificateHateoas giftHateoas;

    @Autowired
    public OrderHateoas(GiftCertificateHateoas hateoas) {
        this.giftHateoas = hateoas;
    }

    @Override
    public void addLinks(OrderDto dto) throws ServiceException, RepositoryException {
        giftHateoas.addLinks(dto.getGift());
    }

    public void setOrderHateoas(DtoPage<OrderDto> page) throws ServiceException, RepositoryException {
        for (OrderDto dto : page.getContent()) {
            addLinks(dto);
        }
    }
}
