package com.epam.esm.generator;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomHandlerTest {
    @Test
    void getRandomNumber() {
        long actual = RandomHandler.getRandomNumber(1, 5);
        assertTrue(actual >= 1 && actual <= 5);
    }

    @Test
    void getRandomWord() {
        String word1 = "word1";
        String word2 = "word2";
        String word3 = "word3";
        List<String> words = List.of(word1,word2,word3);
        String[] strings = words.toArray(new String[0]);
        String actual = RandomHandler.getRandomWord(strings);
        assertTrue(actual.equals(word1) || actual.equals(word2) || actual.equals(word3));
    }
}