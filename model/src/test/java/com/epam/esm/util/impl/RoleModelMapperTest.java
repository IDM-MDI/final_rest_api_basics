package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleModelMapperTest {

    private static RoleModelMapper mapper;
    private static Role entity;
    private static RoleDto dto;
    private static List<Role> entityList;
    private static List<RoleDto> dtoList;

    @BeforeAll
    public static void init() {
        mapper = new RoleModelMapper();
        entity = new Role(
                1L,
                "testRole1"
        );
        dto = new RoleDto(
                1L,
                "testRole1"
                );
        entityList = List.of(
                new Role(
                        2L,
                        "testRole2"),
                new Role(
                        3L,
                        "testRole3"),
                new Role(
                        4L,
                        "testRole4"));
        dtoList = List.of(
                new RoleDto(
                        2L,
                        "testRole2"),
                new RoleDto(
                        3L,
                        "testRole3"),
                new RoleDto(
                        4L,
                        "testRole4"));
    }


    @Test
    void toEntity() {
        Role actual = mapper.toEntity(dto);
        Assertions.assertEquals(entity,actual);
    }

    @Test
    void toDto() {
        RoleDto actual = mapper.toDto(entity);
        Assertions.assertEquals(dto,actual);
    }

    @Test
    void toEntityList() {
        List<Role> actual = mapper.toEntityList(dtoList);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void toDtoList() {
        List<RoleDto> actual = mapper.toDtoList(entityList);
        Assertions.assertEquals(dtoList,actual);
    }
}