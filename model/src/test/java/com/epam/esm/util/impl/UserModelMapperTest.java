package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.builder.impl.UserBuilder;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.StatusDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                new RoleModelMapper(),
                new StatusModelMapper());
        entity = new User(
                1L,
                "testUser1",
                "testPass1",
                null,
                List.of(new Role(1L,"testRole1")),
                new Status(1L,"testStatus1")
        );
        dto = new UserDto(
                1L,
                "testUser1",
                "testPass1",
                null,
                new StatusDto(1L,"testStatus1"),
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
                        new Status(2L,"testStatus2")
                ),
                new User(
                        3L,
                        "testUser3",
                        "testPass3",
                        null,
                        List.of(new Role(3L,"testRole3")),
                        new Status(3L,"testStatus3")
                ),
                new User(
                        4L,
                        "testUser4",
                        "testPass4",
                        null,
                        List.of(new Role(4L,"testRole4")),
                        new Status(4L,"testStatus4")
                ));
        dtoList = List.of(
                new UserDto(
                        2L,
                        "testUser2",
                        "testPass2",
                        null,
                        new StatusDto(2L,"testStatus2"),
                        List.of(new RoleDto(2L,"testRole2")),
                        null
                ),
                new UserDto(
                        3L,
                        "testUser3",
                        "testPass3",
                        null,
                        new StatusDto(3L,"testStatus3"),
                        List.of(new RoleDto(3L,"testRole3")),
                        null
                ),
                new UserDto(
                        4L,
                        "testUser4",
                        "testPass4",
                        null,
                        new StatusDto(4L,"testStatus4"),
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
    void toDto() {
        UserDto actual = mapper.toDto(entity);
        Assertions.assertEquals(dto,actual);
    }

    @Test
    void toEntityList() {
        List<User> actual = mapper.toEntityList(dtoList);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void toDtoList() {
        List<UserDto> actual = mapper.toDtoList(entityList);
        Assertions.assertEquals(dtoList,actual);
    }
}