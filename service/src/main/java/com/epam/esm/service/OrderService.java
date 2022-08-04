package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.service.crud.CRUDDesignPattern;

public interface OrderService extends CRUDDesignPattern<Order, OrderDto> {

}
