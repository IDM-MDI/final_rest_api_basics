package com.epam.esm.util.impl;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.StatusDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusModelMapperTest {

    private static StatusModelMapper mapper;
    private static Status entity;
    private static StatusDto dto;
    private static List<Status> entityList;
    private static List<StatusDto> dtoList;

    @BeforeAll
    public static void init() {
        mapper = new StatusModelMapper();
        entity = new Status(
                1L,
                "testRole1"
        );
        dto = new StatusDto(
                1L,
                "testRole1"
        );
        entityList = List.of(
                new Status(
                        2L,
                        "testRole2"),
                new Status(
                        3L,
                        "testRole3"),
                new Status(
                        4L,
                        "testRole4"));
        dtoList = List.of(
                new StatusDto(
                        2L,
                        "testRole2"),
                new StatusDto(
                        3L,
                        "testRole3"),
                new StatusDto(
                        4L,
                        "testRole4"));
    }


    @Test
    void toEntity() {
        Status actual = mapper.toEntity(dto);
        Assertions.assertEquals(entity,actual);
    }

    @Test
    void toDto() {
        StatusDto actual = mapper.toDto(entity);
        Assertions.assertEquals(dto,actual);
    }

    @Test
    void toEntityList() {
        List<Status> actual = mapper.toEntityList(dtoList);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void toDtoList() {
        List<StatusDto> actual = mapper.toDtoList(entityList);
        Assertions.assertEquals(dtoList,actual);
    }
}