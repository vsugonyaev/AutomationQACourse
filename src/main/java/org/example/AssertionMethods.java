package org.example;

import java.util.ArrayList;
import java.util.List;

public class AssertionMethods {

    public static List<Integer> getDivisors(int n) {
        List<Integer> divisors = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                divisors.add(i);
            }
        }
        return divisors;
    }
    /**
     * Преобразует целое число в римскую строку.
     *
     * @param number число от 1 до 3999 включительно
     * @return римское представление числа
     * @throws IllegalArgumentException если число выходит за допустимые границы
     */
    public static String toRoman(int number) {
        if (number < 1 || number > 3999) {
            throw new IllegalArgumentException("Number must be between 1 and 3999");
        }
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return thousands[number / 1000] +
                hundreds[(number % 1000) / 100] +
                tens[(number % 100) / 10] +
                ones[number % 10];
    }

    public class Calculator {

        // Метод должен складывать два числа, но по ошибке вычитает
        public static int add(int a, int b) {
            return a - b;
        }
    }
}
