package com.epam.esm.builder.impl;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TagBuilderTest {

    private static final TagBuilder builder = new TagBuilder();

    @Test
    @Order(1)
    void setId() {
        TagBuilder actual = builder.setId(1L);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(2)
    void setName() {
        TagBuilder actual = builder.setName("testTag");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(3)
    void setStatus() {
        TagBuilder actual = builder.setStatus("testStatus");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(4)
    void setMainImage() {
        TagBuilder actual = builder.setMainImage(new byte[]{1,1,1,1});
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(5)
    void build() {
        Tag actual = builder.build();
        Tag expected = new Tag(1L,"testTag",new byte[]{1,1,1,1},"testStatus");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Order(6)
    void clear() {
        Tag actual = builder.build();
        Tag expected = new Tag(null,null,null,null);
        Assertions.assertEquals(expected,actual);
    }
}