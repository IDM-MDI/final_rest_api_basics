package com.epam.esm.builder.impl;

import com.epam.esm.entity.Status;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TagBuilderTest {

    private static final TagBuilder builder = new TagBuilder();

    @Test
    @Order(1)
    void setId() {
        TagBuilder actual = builder.setId(1L);
        Assertions.assertEquals(actual,builder);
    }

    @Test
    @Order(2)
    void setName() {
        TagBuilder actual = builder.setName("testTag");
        Assertions.assertEquals(actual,builder);
    }

    @Test
    @Order(3)
    void setStatus() {
        TagBuilder actual = builder.setStatus(new Status(1L,"testStatus"));
        Assertions.assertEquals(actual,builder);
    }

    @Test
    @Order(4)
    void build() {
        Tag actual = builder.build();
        Tag expected = new Tag(1L,"testTag",new Status(1L,"testStatus"));
        Assertions.assertEquals(actual,expected);
    }

    @Test
    @Order(5)
    void clear() {
        Tag actual = builder.build();
        Tag expected = new Tag(null,null,null);
        Assertions.assertEquals(actual,expected);
    }
}