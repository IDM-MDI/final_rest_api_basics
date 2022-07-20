package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.StatusDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                null
        );
        dto = new TagDto(
                1L,
                "testRole1"
        );
        entityList = List.of(
                new Tag(
                        2L,
                        "testRole2",
                        null),
                new Tag(
                        3L,
                        "testRole3",
                        null),
                new Tag(
                        4L,
                        "testRole4",
                        null));
        dtoList = List.of(
                new TagDto(
                        2L,
                        "testRole2"),
                new TagDto(
                        3L,
                        "testRole3"),
                new TagDto(
                        4L,
                        "testRole4"));
    }


    @Test
    void toEntity() {
        Tag actual = mapper.toEntity(dto);
        Assertions.assertEquals(entity,actual);
    }

    @Test
    void toDto() {
        TagDto actual = mapper.toDto(entity);
        Assertions.assertEquals(dto,actual);
    }

    @Test
    void toEntityList() {
        List<Tag> actual = mapper.toEntityList(dtoList);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void toDtoList() {
        List<TagDto> actual = mapper.toDtoList(entityList);
        Assertions.assertEquals(dtoList,actual);
    }
}