package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.StatusDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Status;
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
    private final RoleModelMapper roleMapper;
    private final StatusModelMapper statusMapper;

    @Autowired
    public UserModelMapper(UserBuilder builder,
                           GiftCertificateModelMapper giftMapper,
                           RoleModelMapper roleMapper,
                           StatusModelMapper statusMapper) {
        this.builder = builder;
        this.giftMapper = giftMapper;
        this.roleMapper = roleMapper;
        this.statusMapper = statusMapper;
    }

    @Override
    public User toEntity(UserDto dto) {
        if(dto == null) {
            return null;
        }
        User user = builder
                .setId(dto.getId())
                .setName(dto.getUsername())
                .setStatus(statusMapper.toEntity(dto.getStatus()))
                .build();
        if(dto.getOrders() != null) {
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
        }
        user.setPassword(dto.getPassword());
        user.setRoles(roleMapper.toEntityList(dto.getRoles()));
        return user;
    }

    @Override
    public UserDto toDto(User entity) {
        if(entity == null) {
            return null;
        }
        UserDto result = new UserDto();
        result.setId(entity.getId());
        result.setUsername(entity.getUsername());
        result.setStatus(statusMapper.toDto(entity.getStatus()));

        if(entity.getOrders() != null) {
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
        }

        result.setPassword(entity.getPassword());
        result.setRoles(roleMapper.toDtoList(entity.getRoles()));
        return result;
    }

    @Override
    public List<User> toEntityList(List<UserDto> dtoList) {
        return dtoList == null ? null : dtoList.stream().map(this::toEntity).toList();
    }

    @Override
    public List<UserDto> toDtoList(List<User> entityList) {
        return entityList == null ? null : entityList.stream().map(this::toDto).toList();
    }
}

