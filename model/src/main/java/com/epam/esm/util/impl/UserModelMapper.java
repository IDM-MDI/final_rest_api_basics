package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.UserDto;
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
        return builder.setId(dto.getId()).setName(dto.getName()).
                setOrders(giftMapper.toEntityList(dto.getOrders())).build();
    }

    @Override
    public UserDto toDto(User entity) {
        UserDto result = new UserDto();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setOrders(giftMapper.toDtoList(entity.getOrders()));
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

