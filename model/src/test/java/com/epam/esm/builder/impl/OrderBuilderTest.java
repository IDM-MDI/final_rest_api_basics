package com.epam.esm.builder.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.util.Date;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderBuilderTest {

    private static final OrderBuilder builder = new OrderBuilder();

    @Test
    @Order(1)
    void setId() {
        OrderBuilder actual = builder.setId(1L);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(2)
    void setPrice() {
        OrderBuilder actual = builder.setPrice(new BigDecimal("0"));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(3)
    void setPurchaseTime() {
        OrderBuilder actual = builder.setPurchaseTime(new Date(0));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(4)
    void setGift() {
        OrderBuilder actual = builder.setGift(new GiftCertificate(
                1L,
                "testGift",
                "testDescription",
                new BigDecimal("0"),
                0,
                null,
                null,
                null,
                "testStatus"));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(5)
    void setUser() {
        OrderBuilder actual = builder.setUser(new User(
                1L,
                "testLogin",
                "testPassword",
                null,
                null,
                "testStatus"));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(6)
    void setStatus() {
        OrderBuilder actual = builder.setStatus("testStatus");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(7)
    void build() {
        com.epam.esm.entity.Order actual = builder.build();
        com.epam.esm.entity.Order expected = new com.epam.esm.entity.Order(
                1L,
                new BigDecimal("0"),
                new Date(0),
                new GiftCertificate(
                        1L,
                        "testGift",
                        "testDescription",
                        new BigDecimal("0"),
                        0,
                        null,
                        null,
                        null,
                        "testStatus"),
                new User(
                        1L,
                        "testLogin",
                        "testPassword",
                        null,
                        null,
                        "testStatus"),
                "testStatus"
        );
        Assertions.assertEquals(actual,expected);
    }

    @Test
    @Order(8)
    void clear() {
        com.epam.esm.entity.Order actual = builder.build();
        com.epam.esm.entity.Order expected = new com.epam.esm.entity.Order(
                null,
                null,
                null,
                null,
                null,
                null
        );
        Assertions.assertEquals(actual,expected);
    }
}