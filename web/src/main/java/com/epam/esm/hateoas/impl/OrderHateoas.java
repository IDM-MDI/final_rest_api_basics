package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.HateoasDTO;
import com.epam.esm.hateoas.links.OrderLinks;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderHateoas extends HateoasDTO<OrderDto> {
    private final GiftCertificateHateoas giftHateoas;

    @Autowired
    public OrderHateoas(GiftCertificateHateoas hateoas) {
        this.giftHateoas = hateoas;
    }

    @Override
    public void addLinks(OrderDto dto) throws ServiceException, RepositoryException {
        giftHateoas.addLinks(dto.getGift());
    }

    @SneakyThrows
    @Override
    protected void addPageLink(DtoPage<OrderDto> dtoPage, int number, int size, String sort, String direction, String rel) {
        switch (dtoPage.getType()) {
            case ORDER_USER -> OrderLinks.getOrdersByUser(dtoPage,number,size,sort,direction,rel);
        }
    }
}
