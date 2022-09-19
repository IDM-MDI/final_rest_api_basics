package com.epam.esm.hateoas.links;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.WebException;

import static com.epam.esm.controller.ControllerClass.ORDER_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class OrderLinks {
    private OrderLinks() {}
    public static void getOrdersByUser(DtoPage<OrderDto> dto, int number, int size, String sort, String direction, String rel) throws ServiceException, RepositoryException, WebException {
        dto.add(linkTo(
                methodOn(ORDER_CONTROLLER)
                        .getOrders(number,size,sort,direction))
                .withRel(rel));
    }
}
