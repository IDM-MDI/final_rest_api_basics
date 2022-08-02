package com.epam.esm.builder.impl;

import com.epam.esm.dto.ResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResponseDtoBuilderTest {

    private static final ResponseDtoBuilder builder = new ResponseDtoBuilder();

    @Test
    @Order(1)
    void setCode() {
        ResponseDtoBuilder actual = builder.setCode(1);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(2)
    void setText() {
        ResponseDtoBuilder actual = builder.setText("testText");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(3)
    void build() {
        ResponseDto actual = builder.build();
        ResponseDto expected = new ResponseDto(1,"testText");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Order(4)
    void clear() {
        ResponseDto actual = builder.build();
        ResponseDto expected = new ResponseDto(0,null);
        Assertions.assertEquals(expected,actual);
    }
}