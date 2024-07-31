package org.example.coforge;

import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.in;
import static java.lang.System.out;

public class StringRunner {

    private static final String TESTED_STR = "Hello, Do you like java code?";

    public static void main(String[] args) {
        out.println(palindrome(TESTED_STR));
        out.println(changeLetter('l', 'L', TESTED_STR));
        out.println(calculateLetter('l', TESTED_STR));

        out.println("Is 'Bob' palindrom: " + isPalindrome("Bob"));
        out.println("Is 'boG' palindrom: " + isPalindrome("boG"));

        out.println("Is 'Bob' palindrom: " + isPalindromeAllMatch("Bob"));
        out.println("Is 'boG' palindrom: " + isPalindromeAllMatch("boG"));
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

    private static boolean isPalindrome(String test) {
        var testedString = test.toLowerCase(Locale.ROOT);
        var reverseString = testedString.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list.stream();
                }))
                .map(String::valueOf)
                .collect(Collectors.joining());
        return testedString.equals(reverseString);
    }

    private static boolean isPalindromeAllMatch(String test) {
        var testedString = test.toLowerCase(Locale.ROOT);
        return IntStream.range(0, testedString.length())
                .allMatch(index ->
                        testedString.charAt(index) == testedString.charAt(testedString.length() - index - 1));
    }

    private static boolean isPalindromeRecursive(String str, int left, int right) {
        if (left >= right) {
            return true;
        }
        if (str.charAt(left) != str.charAt(right)) {
            return false;
        }
        return isPalindromeRecursive(str, left + 1, right - 1);
    }

    private static int reverse(int number) {
        return IntStream.iterate(number, n -> n != 0, n -> n / 10)
                .map(n -> n % 10)
                .reduce(0, (reversed, digit) -> reversed * 10 + digit);
    }
}
