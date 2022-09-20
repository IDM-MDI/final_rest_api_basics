package com.epam.esm.controller;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.WebException;
import com.epam.esm.hateoas.impl.OrderHateoas;
import com.epam.esm.service.LoginService;
import com.epam.esm.service.page.PageOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/v1/order")
@Validated
@Profile("prod")
public class OrderController {

    private final PageOrderService service;
    private final OrderHateoas hateoas;
    private final LoginService loginService;

    @Autowired
    public OrderController(PageOrderService service, OrderHateoas hateoas, LoginService loginService) {
        this.service = service;
        this.hateoas = hateoas;
        this.loginService = loginService;
    }

    @GetMapping
    public DtoPage<OrderDto> getOrders(@RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(defaultValue = "id") String sort,
                                       @RequestParam(defaultValue = "asc") String direction) throws RepositoryException, ServiceException, WebException {

        DtoPage<OrderDto> dtoPage = service.findByPage(page,size,sort,direction,loginService.getUsernameByContext());
        hateoas.setHateoas(dtoPage);
        return dtoPage;
    }

    @PostMapping("/{id}")
    public DtoPage<OrderDto> addOrder(@PathVariable @Min(1) long id) throws RepositoryException, ServiceException, WebException {
        DtoPage<OrderDto> dtoPage = service.save(loginService.getUsernameByContext(), id);
        hateoas.setHateoas(dtoPage);
        return dtoPage;
    }

    @PatchMapping("/{id}")
    public DtoPage<OrderDto> updateOrder(@PathVariable @Min(1) long id,@Valid @RequestBody OrderDto entity) throws WebException, ServiceException, RepositoryException {
        DtoPage<OrderDto> dtoPage = service.update(loginService.getUsernameByContext(),entity,id);
        hateoas.setHateoas(dtoPage);
        return dtoPage;
    }

    @DeleteMapping("/{id}")
    public DtoPage<OrderDto> deleteOrder(@PathVariable @Min(1) long id) throws WebException, ServiceException, RepositoryException {
        DtoPage<OrderDto> dtoPage = service.delete(loginService.getUsernameByContext(),id);
        hateoas.setHateoas(dtoPage);
        return dtoPage;
    }
}
