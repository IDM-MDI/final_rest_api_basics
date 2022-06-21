package com.epam.esm.controller;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hateoas.impl.OrderHateoas;
import com.epam.esm.service.LoginService;
import com.epam.esm.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/order")
@Validated
@Profile("prod")
public class OrderController {

    private final OrderService service;
    private final OrderHateoas hateoas;
    private final LoginService loginService;

    @Autowired
    public OrderController(OrderService service, OrderHateoas hateoas, LoginService loginService) {
        this.service = service;
        this.hateoas = hateoas;
        this.loginService = loginService;
    }

    @PostMapping("/{id}")
    public DtoPage<OrderDto> addOrder(@PathVariable @Min(1) long id, HttpServletRequest request) throws RepositoryException, ServiceException {
        DtoPage<OrderDto> dtoPage = service.saveByUserWithDtoPage(loginService.getUsername(request), id);
        hateoas.setOrderHateoas(dtoPage);
        return dtoPage;
    }
}
