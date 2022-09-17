package com.epam.esm.builder.impl;

import com.epam.esm.dto.ControllerType;
import com.epam.esm.dto.DtoPage;
import com.epam.esm.dto.ResponseDto;
import com.epam.esm.dto.TagDto;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DtoPageBuilderTest {

    private static final DtoPageBuilder<TagDto> builder = new DtoPageBuilder<>();
    private static final TagDto CONTENT = new TagDto(1L,"",null,false,ACTIVE.name());

    @Test
    @Order(1)
    void setContent() {
        DtoPageBuilder<TagDto> actual = builder.setContent(List.of(CONTENT));
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
    void setType() {
        DtoPageBuilder<TagDto> actual = builder.setType(ControllerType.TAG_ALL);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(7)
    void setHasNext() {
        DtoPageBuilder<TagDto> actual = builder.setHasNext(true);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(8)
    void setParam() {
        DtoPageBuilder<TagDto> actual = builder.setParam("test param");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(9)
    void setDirection() {
        DtoPageBuilder<TagDto> actual = builder.setDirection("test direction");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(10)
    void build() {
        DtoPage<TagDto> expected = new DtoPage<>(
                List.of(CONTENT),
                new ResponseDto(1,"test"),
                0,
                0,
                "0",
                "test direction",
                true,
                "test param",
                ControllerType.TAG_ALL);
        DtoPage<TagDto> actual = builder.build();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Order(11)
    void clear() {
        DtoPage<TagDto> expected = new DtoPage<>(
                null,
                null,
                0,
                0,
                null,
                null,
                false,
                null,
                null);
        DtoPage<TagDto> actual = builder.build();
        Assertions.assertEquals(expected,actual);
    }
}