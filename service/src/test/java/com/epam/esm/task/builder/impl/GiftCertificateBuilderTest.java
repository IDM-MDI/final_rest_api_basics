package com.epam.esm.task.builder.impl;

import com.epam.esm.task.entity.impl.GiftCertificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateBuilderTest {

    private static final GiftCertificate expected = new GiftCertificate();

    @BeforeAll
    public static void init() {
        expected.setId(1);
        expected.setName("test");
        expected.setDescription("test");
        expected.setPrice(new BigDecimal(55));
        expected.setDuration(10);
        expected.setCreate_date(LocalDateTime.parse("2018-08-29T06:12:15.156"));
        expected.setUpdate_date(LocalDateTime.parse("2018-08-29T06:12:15.156"));
        expected.setTags(new ArrayList<>());
    }

    @Test
    public void getResult() {
        GiftCertificateBuilder builder = new GiftCertificateBuilder();
        GiftCertificate actual = builder.setId(1).setName("test").setDescription("test").
                setPrice(new BigDecimal(55)).setDuration(10).
                setCreate_date(LocalDateTime.parse("2018-08-29T06:12:15.156")).
                setUpdate_date(LocalDateTime.parse("2018-08-29T06:12:15.156")).
                setTagList(new ArrayList<>()).
                getResult();
        Assertions.assertEquals(actual,expected);
    }
}