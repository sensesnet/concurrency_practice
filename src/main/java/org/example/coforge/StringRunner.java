package org.example.coforge;

import java.util.Collections;
import java.util.stream.Collectors;

public class StringRunner {

    private static final String TESTED_STR = "Hello, Do you like java code?";

    public static void main(String[] args) {
        System.out.println(palindrome(TESTED_STR));
        System.out.println(changeLetter('l', 'L', TESTED_STR));
        System.out.println(calculateLetter('l', TESTED_STR));
    }

    private static String palindrome(String str) {
        return str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list.stream();
                }))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static String changeLetter(char letter, char replacement, String str) {
        return str.chars()
                .mapToObj(c -> (char) c)
                .map(c -> c == letter ? replacement : c)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static long calculateLetter(char letter, String str) {
        return str.chars()
                .filter(c -> c == letter)
                .count();
    }
}
