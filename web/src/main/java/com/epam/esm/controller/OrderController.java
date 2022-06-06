package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order")
@Validated
@Profile("prod")
public class OrderController {

    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> addGiftCertificate(@RequestBody OrderDto dto) throws RepositoryException {
        service.saveWithResponse(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }
}
