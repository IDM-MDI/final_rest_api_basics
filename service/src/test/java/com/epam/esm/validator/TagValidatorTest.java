package com.epam.esm.validator;

import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagValidatorTest {

    @Test
    void isTagEqualsToDB() {
        Tag tagFromDB = new Tag(1L,"test",new Status(1L,"active"));
        Tag tag = new Tag(1L,"test",new Status(1L,"active"));
        assertTrue(TagValidator.isTagEqualsToDB(tagFromDB,tag));
    }

}