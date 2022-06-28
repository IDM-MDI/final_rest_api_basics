package com.epam.esm.generator;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class RandomHandler {
    private final Random random = new Random();
    private static int min;
    private static int max;


    public long getRandomNumber(long min,long max) {
        return random.nextLong((max - min) + 1) + min;
    }
    public String getRandomWord(String[] words) {
        min = 0;
        max = 68000;
        return words[random.nextInt((max - min) + 1) + min];
    }

    public Set<String> getCountWords(String[] words, int count) {
        min = 0;
        max = 68000;
        Set<String> setWords = new HashSet<>();
        for (int i = 0; i < count; i++) {
            if(setWords.size() == i) {
                setWords.add(words[(int) getRandomNumber(min,max)]);
            }
            else {
                i = setWords.size()-1;
            }
        }
        return setWords;
    }
}
