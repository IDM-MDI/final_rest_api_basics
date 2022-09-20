package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.epam.esm.entity.StatusName.ACTIVE;

class GiftValidatorTest {

    @Test
    void findEquals() {
        List<GiftCertificate> firstList = List.of(
                new GiftCertificate(
                        1L,
                        "name1",
                        "desc1",
                        new BigDecimal(1),
                        1,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null),
                new GiftCertificate(
                        2L,
                        "name2",
                        "desc1",
                        new BigDecimal(2),
                        2,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null),
                new GiftCertificate(
                        3L,
                        "name3",
                        "desc1",
                        new BigDecimal(3),
                        3,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)
        );
        List<GiftCertificate> secondList = List.of(
                new GiftCertificate(
                        1L,
                        "name1",
                        "desc1",
                        new BigDecimal(1),
                        1,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null),
                new GiftCertificate(
                        3L,
                        "name3",
                        "desc1",
                        new BigDecimal(3),
                        3,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null)
        );
        List<GiftCertificate> actual = GiftValidator.findEquals(firstList, secondList);

        Assertions.assertEquals(secondList,actual);
    }

    @Test
    void isStringsEmpty() {
        Assertions.assertTrue(GiftValidator.isStringEmpty(""));
    }
    @Test
    void isStringsNotEmpty() {
        Assertions.assertFalse(GiftValidator.isStringEmpty("tags"));
    }

    @Test
    void isGiftEmpty() {
        Assertions.assertTrue(GiftValidator.isGiftEmpty(new GiftCertificate()));
    }
    @Test
    void isGiftNotEmpty() {
        Assertions.assertFalse(GiftValidator.isGiftEmpty(new GiftCertificate(
                1L,
                "testName",
                "testDescription",
                new BigDecimal("0"),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null)));
    }

    @Test
    void uniteEntities() {
        GiftCertificate expected = new GiftCertificate(
                3L,
                "name3",
                "desc1",
                new BigDecimal(3),
                3,
                null,
                null,
                null,
                "shop 3",
                new byte[]{1,1,1,1},
                new byte[]{2,2,2,2},
                new byte[]{3,3,3,3},
                ACTIVE.name());
        GiftCertificate actual = GiftValidator.uniteEntities(new GiftCertificate(), expected);
        Assertions.assertEquals(expected,actual);
    }
    @Test
    void uniteEntitiesByUpdate() {
        GiftCertificate expected = new GiftCertificate(
                3L,
                "name3",
                "desc1",
                new BigDecimal(3),
                3,
                null,
                null,
                null,
                "shop 3",
                new byte[]{1,1,1,1},
                new byte[]{2,2,2,2},
                new byte[]{3,3,3,3},
                ACTIVE.name());
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(3L);

        GiftCertificate actual = GiftValidator.uniteEntities(expected, certificate);
        Assertions.assertEquals(expected,actual);
    }
}