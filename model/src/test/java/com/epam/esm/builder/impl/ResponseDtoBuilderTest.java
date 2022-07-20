package com.epam.esm.builder.impl;

import com.epam.esm.dto.ResponseDto;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResponseDtoBuilderTest {

    private static final ResponseDtoBuilder builder = new ResponseDtoBuilder();

    @Test
    @Order(1)
    void setCode() {
        ResponseDtoBuilder actual = builder.setCode(1);
        Assertions.assertEquals(actual,builder);
    }

    @Test
    @Order(2)
    void setText() {
        ResponseDtoBuilder actual = builder.setText("testText");
        Assertions.assertEquals(actual,builder);
    }

    @Test
    @Order(3)
    void build() {
        ResponseDto actual = builder.build();
        ResponseDto expected = new ResponseDto(1,"testText");
        Assertions.assertEquals(actual,expected);
    }

    @Test
    @Order(4)
    void clear() {
        ResponseDto actual = builder.build();
        ResponseDto expected = new ResponseDto(0,null);
        Assertions.assertEquals(actual,expected);
    }
}