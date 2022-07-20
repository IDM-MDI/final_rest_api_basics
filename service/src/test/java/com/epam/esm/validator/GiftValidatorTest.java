package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GiftValidatorTest {

    @Test
    void findEquals() {
        Status active = new Status(1L,"active");
        List<GiftCertificate> gifts = List.of(
                new GiftCertificate(1L,"test1","description1",new BigDecimal("1"),1, null,null,null,active),
                new GiftCertificate(2L,"test2","description2",new BigDecimal("2"),2,null,null,null,active),
                new GiftCertificate(3L,"test3","description3",new BigDecimal("2"),3,null,null,null,active)
        );
        List<GiftCertificate> expected = List.of(
                new GiftCertificate(3L,"test3","description3",new BigDecimal("3"),3,null,null,null,active),
                new GiftCertificate(1L,"test1","description1",new BigDecimal("1"),1,null,null,null,active)
        );
        List<GiftCertificate> actual = GiftValidator.findEquals(gifts, expected);
        assertFalse(
                expected.size() == actual.size() &&
                        expected.containsAll(actual) &&
                        actual.containsAll(expected)
        );
    }

    @Test
    void isTagsEmpty() {
        assertFalse(GiftValidator.isTagsEmpty("Tags"));
    }

    @Test
    void isGiftEmpty() {
        GiftCertificate gift = new GiftCertificate();
        assertTrue(GiftValidator.isGiftEmpty(gift));
    }

    @Test
    void isTagListNotEmpty() {
        assertTrue(GiftValidator.isTagListNotEmpty(List.of(new Tag())));
    }

    @Test
    void uniteEntities() {
        GiftCertificate actual = new GiftCertificate();
        GiftCertificate expected = new GiftCertificate(
                1L,
                "test",
                "test",
                new BigDecimal("0"),
                10,
                null,
                null,
                null,
                null
        );
        GiftValidator.uniteEntities(actual,expected);
        assertEquals(expected,actual);
    }

}