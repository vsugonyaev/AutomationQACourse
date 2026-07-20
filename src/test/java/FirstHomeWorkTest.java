package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FirstHomeWorkTest {

    // ==================== isEven ====================
    @Test
    @DisplayName("isEven: четные числа")
    void isEven_ShouldReturnTrueForEvenNumbers() {
        assertTrue(FirstHomeWork.isEven(2));
        assertTrue(FirstHomeWork.isEven(0));
        assertTrue(FirstHomeWork.isEven(-4));
    }

    @Test
    @DisplayName("isEven: нечетные числа")
    void isEven_ShouldReturnFalseForOddNumbers() {
        assertFalse(FirstHomeWork.isEven(3));
        assertFalse(FirstHomeWork.isEven(-1));
        assertFalse(FirstHomeWork.isEven(7));
    }

    // ==================== checkAccess ====================
    @Test
    @DisplayName("checkAccess: возраст > 18 -> Allowed")
    void checkAccess_ShouldReturnAllowedForAgeGreaterThan18() {
        assertEquals("Allowed", FirstHomeWork.checkAccess(19));
        assertEquals("Allowed", FirstHomeWork.checkAccess(25));
    }

    @Test
    @DisplayName("checkAccess: возраст <= 18 -> Denied")
    void checkAccess_ShouldReturnDeniedForAgeLessOrEqual18() {
        assertEquals("Denied", FirstHomeWork.checkAccess(18));
        assertEquals("Denied", FirstHomeWork.checkAccess(10));
        assertEquals("Denied", FirstHomeWork.checkAccess(0));
    }

    // ==================== isPositive ====================
    @Test
    @DisplayName("isPositive: положительные и ноль")
    void isPositive_ShouldReturnTrueForNonNegative() {
        assertTrue(FirstHomeWork.isPositive(5));
        assertTrue(FirstHomeWork.isPositive(0));
        assertTrue(FirstHomeWork.isPositive(100));
    }

    @Test
    @DisplayName("isPositive: отрицательные")
    void isPositive_ShouldReturnFalseForNegative() {
        assertFalse(FirstHomeWork.isPositive(-1));
        assertFalse(FirstHomeWork.isPositive(-10));
    }

    // ==================== getGrade ====================
    @Test
    @DisplayName("getGrade: корректные оценки")
    void getGrade_ShouldReturnCorrectLetter() {
        assertEquals("A", FirstHomeWork.getGrade(95));
        assertEquals("A", FirstHomeWork.getGrade(100));
        assertEquals("A", FirstHomeWork.getGrade(81));
        assertEquals("B", FirstHomeWork.getGrade(80));
        assertEquals("B", FirstHomeWork.getGrade(61));
        assertEquals("C", FirstHomeWork.getGrade(60));
        assertEquals("C", FirstHomeWork.getGrade(41));
        assertEquals("D", FirstHomeWork.getGrade(40));
        assertEquals("D", FirstHomeWork.getGrade(21));
        assertEquals("E", FirstHomeWork.getGrade(20));
        assertEquals("E", FirstHomeWork.getGrade(1));
        assertEquals("E", FirstHomeWork.getGrade(0));
    }

    @Test
    @DisplayName("getGrade: недопустимые оценки -> Error")
    void getGrade_ShouldReturnErrorForInvalidScores() {
        assertEquals("Error", FirstHomeWork.getGrade(-1));
        assertEquals("Error", FirstHomeWork.getGrade(101));
        assertEquals("Error", FirstHomeWork.getGrade(-100));
        assertEquals("Error", FirstHomeWork.getGrade(200));
    }

    // ==================== blastOff ====================
    @Test
    @DisplayName("blastOff: обычный запуск")
    void blastOff_ShouldReturnCountdownWithSpaces() {
        assertEquals("5 4 3 2 1 Поехали", FirstHomeWork.blastOff(5));
        assertEquals("3 2 1 Поехали", FirstHomeWork.blastOff(3));
        assertEquals("1 Поехали", FirstHomeWork.blastOff(1));
    }

    @Test
    @DisplayName("blastOff: старт с 0")
    void blastOff_ShouldReturnOnlyPoehaliForZero() {
        assertEquals("Поехали", FirstHomeWork.blastOff(0));
    }

    // ==================== sumToN ====================
    @Test
    @DisplayName("sumToN: сумма от 1 до N")
    void sumToN_ShouldReturnCorrectSum() {
        assertEquals(15, FirstHomeWork.sumToN(5));
        assertEquals(1, FirstHomeWork.sumToN(1));
        assertEquals(55, FirstHomeWork.sumToN(10));
        assertEquals(0, FirstHomeWork.sumToN(0));
    }

    // ==================== hasBug ====================
    @Test
    @DisplayName("hasBug: массив содержит 'Bug'")
    void hasBug_ShouldReturnTrueIfBugPresent() {
        String[] arr = {"Hello", "Bug", "World"};
        assertTrue(FirstHomeWork.hasBug(arr));
    }

    @Test
    @DisplayName("hasBug: массив не содержит 'Bug'")
    void hasBug_ShouldReturnFalseIfBugAbsent() {
        String[] arr = {"Hello", "World", "fix"};
        assertFalse(FirstHomeWork.hasBug(arr));
    }

    @Test
    @DisplayName("hasBug: игнорирование регистра")
    void hasBug_ShouldIgnoreCase() {
        String[] arr = {"BUG", "bUg", "BuG"};
        assertTrue(FirstHomeWork.hasBug(arr)); // хотя бы одно совпадение
    }

    // ==================== getEvenInRange ====================
    @Test
    @DisplayName("getEvenInRange: обычный диапазон")
    void getEvenInRange_ShouldReturnEvenNumbersWithSpaces() {
        assertEquals("2 4", FirstHomeWork.getEvenInRange(2, 5));
        assertEquals("-4 -2 0", FirstHomeWork.getEvenInRange(-4, 0));
        assertEquals("10", FirstHomeWork.getEvenInRange(10, 10));
        assertEquals("", FirstHomeWork.getEvenInRange(1, 1)); // нет четных
    }

    @Test
    @DisplayName("getEvenInRange: start > end -> пустая строка")
    void getEvenInRange_ShouldReturnEmptyForInvalidRange() {
        assertEquals("", FirstHomeWork.getEvenInRange(5, 2));
        assertEquals("", FirstHomeWork.getEvenInRange(0, -1));
    }

    // ==================== findMax ====================
    @Test
    @DisplayName("findMax: массив с положительными и отрицательными")
    void findMax_ShouldReturnMaxValue() {
        int[] arr1 = {3, 7, 2, 9, 1};
        assertEquals(9, FirstHomeWork.findMax(arr1));

        int[] arr2 = {-5, -2, -8, -1};
        assertEquals(-1, FirstHomeWork.findMax(arr2));

        int[] arr3 = {0, -10, 5, -3};
        assertEquals(5, FirstHomeWork.findMax(arr3));

        int[] arr4 = {42};
        assertEquals(42, FirstHomeWork.findMax(arr4));
    }

    // ==================== reverse ====================
    @Test
    @DisplayName("reverse: обычный массив")
    void reverse_ShouldReturnReversedArray() {
        String[] input = {"a", "b", "c", "d"};
        String[] expected = {"d", "c", "b", "a"};
        assertArrayEquals(expected, FirstHomeWork.reverse(input));
    }

    @Test
    @DisplayName("reverse: массив из одного элемента")
    void reverse_ShouldReturnSameForSingleElement() {
        String[] input = {"only"};
        assertArrayEquals(input, FirstHomeWork.reverse(input));
    }

    @Test
    @DisplayName("reverse: пустой массив")
    void reverse_ShouldReturnEmptyArray() {
        String[] input = {};
        assertArrayEquals(input, FirstHomeWork.reverse(input));
    }

    // ==================== calcAverage ====================
    @Test
    @DisplayName("calcAverage: обычные списки")
    void calcAverage_ShouldReturnIntegerAverage() {
        List<Integer> list1 = Arrays.asList(2, 4, 6, 8);
        assertEquals(5, FirstHomeWork.calcAverage(list1));

        List<Integer> list2 = Arrays.asList(-5, 0, 5);
        assertEquals(0, FirstHomeWork.calcAverage(list2));

        List<Integer> list3 = Arrays.asList(10);
        assertEquals(10, FirstHomeWork.calcAverage(list3));
    }

    @Test
    @DisplayName("calcAverage: пустой список -> ArithmeticException")
    void calcAverage_ShouldThrowExceptionForEmptyList() {
        List<Integer> empty = new ArrayList<>();
        assertThrows(ArithmeticException.class, () -> FirstHomeWork.calcAverage(empty));
    }

    // ==================== removeSpecificName ====================
    @Test
    @DisplayName("removeSpecificName: удаление существующего имени")
    void removeSpecificName_ShouldRemoveAllOccurrences() {
        List<String> input = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie", "Alice"));
        List<String> expected = Arrays.asList("Bob", "Charlie");
        assertEquals(expected, FirstHomeWork.removeSpecificName(input, "Alice"));
    }

    @Test
    @DisplayName("removeSpecificName: имя отсутствует")
    void removeSpecificName_ShouldReturnSameListIfNameNotPresent() {
        List<String> input = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        List<String> expected = Arrays.asList("Alice", "Bob");
        assertEquals(expected, FirstHomeWork.removeSpecificName(input, "Charlie"));
    }

    @Test
    @DisplayName("removeSpecificName: пустой список")
    void removeSpecificName_ShouldReturnEmptyList() {
        List<String> input = new ArrayList<>();
        assertTrue(FirstHomeWork.removeSpecificName(input, "any").isEmpty());
    }

    @Test
    @DisplayName("removeSpecificName: список содержит null -> NullPointerException")
    void removeSpecificName_ShouldThrowNPEWhenListContainsNull() {
        List<String> input = new ArrayList<>(Arrays.asList("Alice", null, "Bob"));
        // Метод не обрабатывает null, поэтому ожидаем NPE
        assertThrows(NullPointerException.class, () -> FirstHomeWork.removeSpecificName(input, "Alice"));
    }

    // Дополнительный тест: исходный список не изменяется
    @Test
    @DisplayName("removeSpecificName: исходный список не должен меняться")
    void removeSpecificName_ShouldNotModifyOriginalList() {
        List<String> original = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        List<String> copy = new ArrayList<>(original);
        FirstHomeWork.removeSpecificName(original, "Alice");
        assertEquals(copy, original);
    }
}