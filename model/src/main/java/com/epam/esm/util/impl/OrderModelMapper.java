package com.epam.esm.util.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderModelMapper implements ModelMapper<Order, OrderDto> {

    private final GiftCertificateModelMapper giftMapper;

    @Autowired
    public OrderModelMapper(GiftCertificateModelMapper giftMapper) {
        this.giftMapper = giftMapper;
    }

    @Override
    public Order toEntity(OrderDto dto) {
        Order result = new Order();
        result.setId(dto.getId());
        result.setPurchaseTime(dto.getPurchaseTime());
        result.setGift(giftMapper.toEntity(dto.getGift()));
        result.setUser(new User(dto.getUserId(),"",null,null,null));
        result.setPrice(dto.getPrice());
        result.setStatus(null);
        return result;
    }

    @Override
    public OrderDto toDto(Order entity) {
        return new OrderDto(entity.getId(),
                            entity.getPrice(),
                            entity.getPurchaseTime(),
                            giftMapper.toDto(entity.getGift()),
                            entity.getGift().getId(),
                            entity.getUser().getId());
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
