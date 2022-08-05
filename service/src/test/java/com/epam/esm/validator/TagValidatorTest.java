package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TagValidatorTest {

    @Test
    void isTagEqualsToDB() {
        Tag tagFromDB = new Tag(1L,"tag",null);
        Tag tag = new Tag(2L,"tag",null);
        Assertions.assertTrue(TagValidator.isTagEqualsToDB(tagFromDB,tag));
    }

    @Test
    void isListTagEmpty() {
        List<Tag> emptyTag = new ArrayList<>();
        Assertions.assertTrue(TagValidator.isListTagEmpty(emptyTag));
    }
    @Test
    void isListTagNotEmpty() {
        List<Tag> notEmptyTag = List.of(new Tag(), new Tag(), new Tag());
        Assertions.assertFalse(TagValidator.isListTagEmpty(notEmptyTag));
    }
    @Test
    void isListTagNotNull() {
        Assertions.assertTrue(TagValidator.isListTagEmpty(null));
    }
}