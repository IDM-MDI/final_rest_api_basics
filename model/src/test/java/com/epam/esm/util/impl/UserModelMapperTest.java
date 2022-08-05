package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class UserModelMapperTest {
    private static UserModelMapper mapper;
    private static User entity;
    private static UserDto dto;
    private static List<User> entityList;
    private static List<UserDto> dtoList;

    @BeforeAll
    public static void init() {
        mapper = new UserModelMapper(
                new UserBuilder(),
                new GiftCertificateModelMapper(new TagModelMapper(new TagBuilder()),new GiftCertificateBuilder()),
                new RoleModelMapper());
        entity = new User(
                1L,
                "testUser1",
                "testPass1",
                null,
                List.of(new Role(1L,"testRole1")),
                "testStatus1"
        );
        dto = new UserDto(
                1L,
                "testUser1",
                "testPass1",
                null,
                "testStatus1",
                List.of(new RoleDto(1L,"testRole1")),
                null
        );
        entityList = List.of(
                new User(
                        2L,
                        "testUser2",
                        "testPass2",
                        null,
                        List.of(new Role(2L,"testRole2")),
                        "testStatus2"
                ),
                new User(
                        3L,
                        "testUser3",
                        "testPass3",
                        null,
                        List.of(new Role(3L,"testRole3")),
                        "testStatus3"
                ),
                new User(
                        4L,
                        "testUser4",
                        "testPass4",
                        null,
                        List.of(new Role(4L,"testRole4")),
                        "testStatus4"
                ));
        dtoList = List.of(
                new UserDto(
                        2L,
                        "testUser2",
                        "testPass2",
                        null,
                        "testStatus2",
                        List.of(new RoleDto(2L,"testRole2")),
                        null
                ),
                new UserDto(
                        3L,
                        "testUser3",
                        "testPass3",
                        null,
                        "testStatus3",
                        List.of(new RoleDto(3L,"testRole3")),
                        null
                ),
                new UserDto(
                        4L,
                        "testUser4",
                        "testPass4",
                        null,
                        "testStatus4",
                        List.of(new RoleDto(4L,"testRole4")),
                        null
                ));
    }


    @Test
    void toEntity() {
        User actual = mapper.toEntity(dto);
        Assertions.assertEquals(entity,actual);
    }

    @Test
    void toEntityNull() {
        User actual = mapper.toEntity(dto);
        Assertions.assertNull(actual);
    }

    @Test
    void toDto() {
        UserDto actual = mapper.toDto(entity);
        Assertions.assertEquals(dto,actual);
    }

    @Test
    void toDtoNull() {
        UserDto actual = mapper.toDto(entity);
        Assertions.assertNull(actual);
    }

    @Test
    void toEntityList() {
        List<User> actual = mapper.toEntityList(dtoList);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void toEntityListNull() {
        List<User> actual = mapper.toEntityList(dtoList);
        Assertions.assertNull(actual);
    }

    @Test
    void toDtoList() {
        List<UserDto> actual = mapper.toDtoList(entityList);
        Assertions.assertEquals(dtoList,actual);
    }

    @Test
    void toDtoListNull() {
        List<UserDto> actual = mapper.toDtoList(entityList);
        Assertions.assertNull(actual);
    }
}