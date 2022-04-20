package com.epam.esm.task.builder.impl;

import com.epam.esm.task.entity.impl.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TagBuilderTest {

    private final Tag expected = new Tag(1,"name");

    @Test
    void getResult() {
        TagBuilder builder = new TagBuilder();
        Tag actual = builder.setId(1).setName("name").getResult();
        Assertions.assertEquals(expected,actual);
    }
}