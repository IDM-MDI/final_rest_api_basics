package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class GiftValidatorTest {

    @Test
    void findEquals() {
        List<GiftCertificate> firstList = List.of(
                new GiftCertificate(1L,"name1","desc1",new BigDecimal(1),1,null,null,null,null),
                new GiftCertificate(2L,"name2","desc1",new BigDecimal(2),2,null,null,null,null),
                new GiftCertificate(3L,"name3","desc1",new BigDecimal(3),3,null,null,null,null)
        );
        List<GiftCertificate> secondList = List.of(
                new GiftCertificate(1L,"name1","desc1",new BigDecimal(1),1,null,null,null,null),
                new GiftCertificate(3L,"name3","desc1",new BigDecimal(3),3,null,null,null,null)
        );
        List<GiftCertificate> actual = GiftValidator.findEquals(firstList, secondList);

        Assertions.assertEquals(secondList,actual);
    }

    @Test
    void isTagsEmpty() {
        Assertions.assertTrue(GiftValidator.isStringEmpty(""));
    }
    @Test
    void isTagsNotEmpty() {
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
                null,null,null,null,null
        )));
    }

    @Test
    void uniteEntities() {
        GiftCertificate expected = new GiftCertificate(3L, "name3", "desc1", new BigDecimal(3), 3, null, null, null, null);
        GiftCertificate actual = new GiftCertificate();
        GiftValidator.uniteEntities(actual,expected);
        Assertions.assertEquals(expected,actual);
    }
}