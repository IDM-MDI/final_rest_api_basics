package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TagValidatorTest {

    @Test
    void isTagEqualsToDB() {
        Tag tagFromDB = new Tag(1L,"tag",null);
        Tag tag = new Tag(2L,"tag",null);
        Assertions.assertTrue(TagValidator.isTagEqualsToDB(tagFromDB,tag));
    }
}