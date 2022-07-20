package com.epam.esm.builder.impl;

import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DtoPageBuilderTest {

    private static final DtoPageBuilder<TagDto> builder = new DtoPageBuilder<>();
    private static TagDto content = new TagDto(1L,"");

    @Test
    @Order(1)
    void setContent() {
        DtoPageBuilder<TagDto> actual = builder.setContent(List.of(content));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(2)
    void setSize() {
        DtoPageBuilder<TagDto> actual = builder.setSize(0);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(3)
    void setNumberOfPage() {
        DtoPageBuilder<TagDto> actual = builder.setNumberOfPage(0);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(4)
    void setSortBy() {
        DtoPageBuilder<TagDto> actual = builder.setSortBy("0");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(5)
    void setResponse() {
        DtoPageBuilder<TagDto> actual = builder.setResponse(new ResponseDto(1,"test"));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(6)
    void build() {
        DtoPage<TagDto> expected = new DtoPage<>(
                List.of(content),
                new ResponseDto(1,"test"),
                0,
                0,
                "0");
        DtoPage<TagDto> actual = builder.build();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Order(7)
    void clear() {
        DtoPage<TagDto> expected = new DtoPage<>(
                null,
                null,
                0,
                0,
                null);
        DtoPage<TagDto> actual = builder.build();
        Assertions.assertEquals(expected,actual);
    }
}