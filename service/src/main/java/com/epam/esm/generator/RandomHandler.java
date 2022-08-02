package com.epam.esm.generator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomHandler {
    private static final Random random = new Random();
    private static int min;
    private static int max;


    private RandomHandler(){}

    public static long getRandomNumber(long min,long max) {
        return random.nextLong((max - min) + 1) + min;
    }
    public static String getRandomWord(String[] words) {
        min = 0;
        max = words.length-1;
        long randomNumber = getRandomNumber(min, max);
        return words[(int) randomNumber];
    }

    public static Set<String> getCountWords(String[] words, int count) {
        min = 0;
        max = words.length-1;
        Set<String> setWords = new HashSet<>();
        for (int i = 0; i < count; i++) {
            if(setWords.size() == i) {
                setWords.add(words[(int) getRandomNumber(min,max)]);
            }
            else {
                i = setWords.size() - 1;
            }
        }
        return setWords;
    }
}
