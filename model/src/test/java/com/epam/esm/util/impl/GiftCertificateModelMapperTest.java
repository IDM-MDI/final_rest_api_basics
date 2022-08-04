package com.epam.esm.util.impl;

import com.epam.esm.builder.impl.GiftCertificateBuilder;
import com.epam.esm.builder.impl.TagBuilder;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class GiftCertificateModelMapperTest {

    private static GiftCertificateModelMapper mapper;
    private static GiftCertificate entity;
    private static GiftCertificateDto dto;
    private static List<GiftCertificate> entityList;
    private static List<GiftCertificateDto> dtoList;

    @BeforeAll
    public static void init() {
        mapper = new GiftCertificateModelMapper(
                new TagModelMapper(new TagBuilder()),
                new GiftCertificateBuilder());
        entity = new GiftCertificate(
                1L,
                "testGift",
                "test",
                new BigDecimal("0"),
                0,
                LocalDateTime.of(2000,1, 1,0,5),
                LocalDateTime.of(1,1,1,1,1),
                List.of(new Tag(1L,"testTag","testStatus")),
                "testStatus");
        dto = new GiftCertificateDto(
                1L,
                "testGift",
                "test",
                new BigDecimal("0"),
                0,
                LocalDateTime.of(2000,1, 1,0,5),
                LocalDateTime.of(1,1,1,1,1),
                List.of(new TagDto(1L,"testTag","testStatus")),
                "testStatus");
        entityList = List.of(new GiftCertificate(
                2L,
                "testGift2",
                "test2",
                new BigDecimal("2"),
                2,
                LocalDateTime.of(2000,1, 1,0,5),
                LocalDateTime.of(2,2,2,2,2),
                List.of(new Tag(2L,"testTag2","testStatus")),
                "testStatus"),
                new GiftCertificate(
                        3L,
                        "testGift3",
                        "test3",
                        new BigDecimal("3"),
                        3,
                        LocalDateTime.of(2000,1, 1,0,5),
                        LocalDateTime.of(3,3,3,3,3),
                        List.of(new Tag(3L,"testTag3","testStatus")),
                        "testStatus"),
                new GiftCertificate(
                        4L,
                        "testGift4",
                        "test4",
                        new BigDecimal("4"),
                        4,
                        LocalDateTime.of(2000,1, 1,0,5),
                        LocalDateTime.of(4,4,4,4,4),
                        List.of(new Tag(4L,"testTag4","testStatus")),
                        "testStatus"));
        dtoList = List.of(new GiftCertificateDto(
                        2L,
                        "testGift2",
                        "test2",
                        new BigDecimal("2"),
                        2,
                        LocalDateTime.of(2000,1, 1,0,5),
                        LocalDateTime.of(2,2,2,2,2),
                        List.of(new TagDto(2L,"testTag2","testStatus")),
                        "testStatus"),
                new GiftCertificateDto(
                        3L,
                        "testGift3",
                        "test3",
                        new BigDecimal("3"),
                        3,
                        LocalDateTime.of(2000,1, 1,0,5),
                        LocalDateTime.of(3,3,3,3,3),
                        List.of(new TagDto(3L,"testTag3","testStatus")),
                        "testStatus"),
                new GiftCertificateDto(
                        4L,
                        "testGift4",
                        "test4",
                        new BigDecimal("4"),
                        4,
                        LocalDateTime.of(2000,1, 1,0,5),
                        LocalDateTime.of(4,4,4,4,4),
                        List.of(new TagDto(4L,"testTag4","testStatus")),
                        "testStatus"
                )
        );
    }

    @Test
    void toEntity() {
        GiftCertificate actual = mapper.toEntity(dto);
        Assertions.assertEquals(entity,actual);
    }

    @Test
    void toDto() {
        GiftCertificateDto actual = mapper.toDto(entity);
        Assertions.assertEquals(dto,actual);
    }

    @Test
    void toEntityList() {
        List<GiftCertificate> actual = mapper.toEntityList(dtoList);
        Assertions.assertEquals(entityList,actual);
    }

    @Test
    void toDtoList() {
        List<GiftCertificateDto> actual = mapper.toDtoList(entityList);
        Assertions.assertEquals(dtoList,actual);
    }
}