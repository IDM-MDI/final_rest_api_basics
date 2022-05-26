package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class UserModelMapper implements ModelMapper<User, UserDto> {

    private final UserBuilder builder;
    private final GiftCertificateModelMapper giftMapper;

    @Autowired
    public UserModelMapper(UserBuilder builder, GiftCertificateModelMapper giftMapper) {
        this.builder = builder;
        this.giftMapper = giftMapper;
    }

    @Override
    public User toEntity(UserDto dto) {
        User user = builder.setId(dto.getId()).setName(dto.getName()).build();
        List<Order> orders = new ArrayList<>();
        dto.getOrders().forEach(order -> {
            Order entity = new Order();
            entity.setId(order.getId());
            entity.setPrice(order.getPrice());
            entity.setPurchaseTime(order.getPurchaseTime());
            entity.setUser(user);
            entity.setGift(giftMapper.toEntity(order.getGift()));
            orders.add(entity);
        });
        user.setOrders(orders);
        return user;
    }

    @Override
    public UserDto toDto(User entity) {
        UserDto result = new UserDto();
        result.setId(entity.getId());
        result.setName(entity.getName());
        List<OrderDto> orders = new ArrayList<>();
        entity.getOrders().forEach(order -> {
            OrderDto dto = new OrderDto();
            dto.setId(order.getId());
            dto.setPrice(order.getPrice());
            dto.setPurchaseTime(order.getPurchaseTime());
            dto.setGift(giftMapper.toDto(order.getGift()));
            dto.setUserId(order.getUser().getId());
            dto.setGiftId(order.getGift().getId());
            orders.add(dto);
        });
        result.setOrders(orders);
        return result;
    }

    @Override
    public List<User> toEntityList(List<UserDto> dtoList) {
        return dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<UserDto> toDtoList(List<User> entityList) {
        return entityList.stream().map(this::toDto).toList();
    }
}

