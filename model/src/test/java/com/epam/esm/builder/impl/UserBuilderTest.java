package com.epam.esm.builder.impl;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserBuilderTest {

    private static final UserBuilder builder = new UserBuilder();

    @Test
    @Order(1)
    void setId() {
        UserBuilder actual = builder.setId(1L);
        Assertions.assertEquals(builder,actual);
    }


    @Test
    @Order(2)
    void setName() {
        UserBuilder actual = builder.setName("testUser");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(3)
    void setOrders() {
        UserBuilder actual = builder.setOrders(null);
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(4)
    void setStatus() {
        UserBuilder actual = builder.setStatus("testStatus");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(5)
    void setPassword() {
        UserBuilder actual = builder.setPassword("testPassword");
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(6)
    void setRoles() {
        UserBuilder actual = builder.setRoles(List.of(new Role(1L,"testRole")));
        Assertions.assertEquals(builder,actual);
    }

    @Test
    @Order(7)
    void build() {
        User actual = builder.build();
        User expected = new User(
                1L,
                "testUser",
                "testPassword",
                null,
                List.of(new Role(1L,"testRole")),
                "testStatus"
        );
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Order(8)
    void clear() {
        User actual = builder.build();
        User expected = new User(
                null,
                null,
                null,
                null,
                null,
                null
        );
        Assertions.assertEquals(expected,actual);
    }
}