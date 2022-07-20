package com.epam.esm.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashGeneratorTest {

    @Test
    void generateByMD5() {
        String password = "password";
        String expected = "5F4DCC3B5AA765D61D8327DEB882CF99";
        String actual = HashGenerator.generateByMD5(password);
        assertEquals(expected,actual);
    }
}