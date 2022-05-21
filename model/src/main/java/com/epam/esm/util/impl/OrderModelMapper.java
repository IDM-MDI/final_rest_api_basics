package com.epam.esm.util.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderModelMapper implements ModelMapper<Order, OrderDto> {

    private final UserModelMapper userModelMapper;
    private final GiftCertificateModelMapper giftModelMapper;

    @Autowired
    public OrderModelMapper(UserModelMapper userModelMapper, GiftCertificateModelMapper giftModelMapper) {
        this.userModelMapper = userModelMapper;
        this.giftModelMapper = giftModelMapper;
    }

    @Override
    public Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setPrice(dto.getPrice());
        order.setPurchaseTime(dto.getPurchaseTime());
        order.setUser(userModelMapper.toEntity(dto.getUser()));
        order.setGift(giftModelMapper.toEntity(dto.getGift()));
        return order;
    }

    @Override
    public OrderDto toDto(Order entity) {
        OrderDto order = new OrderDto();
        order.setId(entity.getId());
        order.setPrice(entity.getPrice());
        order.setPurchaseTime(entity.getPurchaseTime());
        order.setUser(userModelMapper.toDto(entity.getUser()));
        order.setGift(giftModelMapper.toDto(entity.getGift()));
        return order;
    }

    @Override
    public List<Order> toEntityList(List<OrderDto> dtoList) {
        return dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<OrderDto> toDtoList(List<Order> entityList) {
        return entityList.stream().map(this::toDto).toList();
    }
}
