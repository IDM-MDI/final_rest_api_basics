package com.epam.esm.builder.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GiftCertificateBuilderTest {

    private static final GiftCertificateBuilder builder = new GiftCertificateBuilder();

    @Test
    @Order(1)
    void setId() {
        GiftCertificateBuilder actual = builder.setId(1L);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(2)
    void setName() {
        GiftCertificateBuilder actual = builder.setName("testGift");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(3)
    void setDescription() {
        GiftCertificateBuilder actual = builder.setDescription("test");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(4)
    void setStatus() {
        GiftCertificateBuilder actual = builder.setStatus("testStatus");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(5)
    void setTagList() {
        GiftCertificateBuilder actual = builder.setTagList(List.of(new Tag(1L,"testTag",null,"testStatus")));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(6)
    void setPrice() {
        GiftCertificateBuilder actual = builder.setPrice(new BigDecimal("0"));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(7)
    void setDuration() {
        GiftCertificateBuilder actual = builder.setDuration(0);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(8)
    void setCreateDate() {
        GiftCertificateBuilder actual = builder.setCreateDate(LocalDateTime.of(2000,1, 1,0,5));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(9)
    void setUpdateDate() {
        GiftCertificateBuilder actual = builder.setUpdateDate(LocalDateTime.of(1,1,1,1,1));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(10)
    void setMainImage() {
        GiftCertificateBuilder actual = builder.setMainImage(new byte[]{1,1,1,1,1});
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(11)
    void setSecondImage() {
        GiftCertificateBuilder actual = builder.setSecondImage(new byte[]{2,2,2,2,2});
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(12)
    void setThirdImage() {
        GiftCertificateBuilder actual = builder.setThirdImage(new byte[]{3,3,3,3,3});
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(13)
    void setShop() {
        GiftCertificateBuilder actual = builder.setShop("test shop");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(14)
    void build() {
        GiftCertificate actual = builder.build();
        GiftCertificate expected = new GiftCertificate(
                1L,
                "testGift",
                "test",
                new BigDecimal("0"),
                0,
                LocalDateTime.of(2000,1, 1,0,5),
                LocalDateTime.of(1,1,1,1,1),
                List.of(new Tag(1L,"testTag",null,"testStatus")),
                "test shop",
                new byte[]{1,1,1,1,1},
                new byte[]{2,2,2,2,2},
                new byte[]{3,3,3,3,3},
                "testStatus");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Order(15)
    void clear() {
        GiftCertificate actual = builder.build();
        GiftCertificate expected = new GiftCertificate(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new ArrayList<>(),
                null,
                null,
                null,
                null,
                null);
        Assertions.assertEquals(expected,actual);
    }
}