package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class TagModelMapperTest {

    private static TagModelMapper mapper;
    private static Tag entity;
    private static TagDto dto;
    private static List<Tag> entityList;
    private static List<TagDto> dtoList;

    @BeforeAll
    public static void init() {
        mapper = new TagModelMapper(new TagBuilder());
        entity = new Tag(
                1L,
                "testRole1",
                null,
                "testStatus"
        );
        dto = new TagDto(
                1L,
                "testRole1",
                null,false,
                "testStatus"
        );
        entityList = List.of(
                new Tag(
                        2L,
                        "testRole2",
                        null,
                        "testStatus"),
                new Tag(
                        3L,
                        "testRole3",
                        null,
                        "testStatus"),
                new Tag(
                        4L,
                        "testRole4",
                        null,
                        "testStatus"));
        dtoList = List.of(
                new TagDto(
                        2L,
                        "testRole2",
                        null,false,
                        "testStatus"),
                new TagDto(
                        3L,
                        "testRole3",
                        null,false,
                        "testStatus"),
                new TagDto(
                        4L,
                        "testRole4",
                        null,false,
                        "testStatus"));
    }


    @Test
    void toEntity() {
        Tag actual = mapper.toEntity(dto);
        Assertions.assertEquals(entity,actual);
    }

    @Test
    void toEntityNull() {
        Tag actual = mapper.toEntity(null);
        Assertions.assertNull(actual);
    }

    @Test
    void toDto() {
        TagDto actual = mapper.toDto(entity);
        Assertions.assertEquals(dto,actual);
    }
    @Test
    void toDtoNull() {
        TagDto actual = mapper.toDto(null);
        Assertions.assertNull(actual);
    }

    @Test
    void toEntityList() {
        List<Tag> actual = mapper.toEntityList(dtoList);
        Assertions.assertEquals(entityList,actual);
    }
    @Test
    void toEntityListNull() {
        List<Tag> actual = mapper.toEntityList(null);
        Assertions.assertNull(actual);
    }


    @Test
    void toDtoList() {
        List<TagDto> actual = mapper.toDtoList(entityList);
        Assertions.assertEquals(dtoList,actual);
    }
    @Test
    void toDtoListNull() {
        List<TagDto> actual = mapper.toDtoList(null);
        Assertions.assertNull(actual);
    }
}