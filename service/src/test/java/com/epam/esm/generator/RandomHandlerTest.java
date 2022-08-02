package com.epam.esm.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomHandlerTest {
    private final String word1 = "word1";
    private final String word2 = "word2";
    private final String word3 = "word3";
    @Test
    void getRandomNumber() {
        long actual = RandomHandler.getRandomNumber(0, 5);
        assertTrue(actual >= 0 && actual <= 5);
    }

    @Test
    void getRandomWord() {
        List<String> words = List.of(word1,word2,word3);
        String[] strings = words.toArray(new String[0]);
        String actual = RandomHandler.getRandomWord(strings);
        assertTrue(actual.equals(word1) || actual.equals(word2) || actual.equals(word3));
    }

    @Test
    void getCountWords() {
        Set<String> actualSet = RandomHandler.getCountWords(new String[]{word1, word2, word2}, 1);
        String actual = actualSet.stream().findFirst().orElseThrow();
        Assertions.assertTrue(actual.equals(word1) || actual.equals(word2) || actual.equals(word3));
    }
}