package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListValidatorTest {

    @Test
    void isListEmpty() {
        List<Tag> notEmptyTag = List.of(new Tag(), new Tag(), new Tag());
        assertFalse(ListValidator.isListEmpty(notEmptyTag));
    }

    @Test
    void isListNotEmpty() {
        List<Tag> emptyTag = List.of();
        assertTrue(ListValidator.isListEmpty(emptyTag));
    }
}