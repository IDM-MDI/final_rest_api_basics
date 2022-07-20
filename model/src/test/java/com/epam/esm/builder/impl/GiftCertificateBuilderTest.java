package com.epam.esm.builder.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        GiftCertificateBuilder actual = builder.setStatus(new Status(1L,"testStatus"));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(5)
    void setTagList() {
        GiftCertificateBuilder actual = builder.setTagList(List.of(new Tag(1L,"testTag",new Status(1L,"testStatus"))));
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
    void setCreate_date() {
        GiftCertificateBuilder actual = builder.setCreate_date(LocalDateTime.of(2000,1, 1,0,5));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(9)
    void setUpdate_date() {
        GiftCertificateBuilder actual = builder.setUpdate_date(LocalDateTime.of(1,1,1,1,1));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(10)
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
                List.of(new Tag(1L,"testTag",new Status(1L,"testStatus"))),
                new Status(1L,"testStatus"));
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Order(11)
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
                null);
        Assertions.assertEquals(expected,actual);
    }
}