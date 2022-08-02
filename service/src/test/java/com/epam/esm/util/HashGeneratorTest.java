package com.epam.esm.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashGeneratorTest {

    @Test
    void generateByMD5() {
        String password = "password";
        String expected = "5E884898DA28047151D0E56F8DC6292773603D0D6AABBDD62A11EF721D1542D8";
        String actual = HashGenerator.generateBySHA(password);
        assertEquals(expected,actual);
    }
}