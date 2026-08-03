package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.example.FirstHomeWork.*;

class FirstHomeWorkTest {
    private static final Random random = new Random();
    private static final String pass = "TEST PASSED";
    private static final String fail = "TEST FAILED";

    @BeforeEach
    void start() {
        System.out.println("==============================");
        System.out.println("Test method start");
    }
    @AfterEach
    void finish()  {
        System.out.println("Test method end");
        System.out.println("==============================");
    }
    @Test
    @DisplayName("Test of method isEven")
    void testIsEven() {
        int n = random.nextInt(0, 100);
        boolean actualResult = isEven(n);
        if (actualResult){
            System.out.println("Number " + n + " is even!");
        } else {
            System.out.println("Number " + n + " is NOT even!");
        }
    }

    @RepeatedTest(20)
    @DisplayName("Test of method checkAccess")
    void testCheckAccess() {
        int age = random.nextInt(1, 99);
        String actualResult = checkAccess(age);
        if (actualResult.equals("Allowed")) {
            System.out.println("For person with age "+ age + " access allowed.");
        } else {
            System.out.println("For person with age "+ age + " access denied.");
        }
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    @DisplayName("Test of method getGrade")
    void testGetGrade(int value) {
        String grade = getGrade(value);
        System.out.println("For result "+ value + " grade is: " + grade);
    }
    static List<Integer> getTestData(){
        List<Integer> testData = new ArrayList<>();
        int size = 25;
        for (int i = 0; i < size; i++) {
            testData.add(random.nextInt(0, 100));
        }
        return testData.stream().toList();
    }

    @RepeatedTest(5)
    @DisplayName("Test of method isPositive")
    void testIsPositive() {
        int n = random.nextInt(-100, 100) ;
        boolean actualResult = isPositive(n);
        if (n > 0 && actualResult || n < 0 && !actualResult) {
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/blastOff-data.csv", delimiter = ',', numLinesToSkip = 1)
    @DisplayName("Test of method blastOff")
    void testBlastOff(int start, String expectedResult) {
        String actualResult = blastOff(start);
        if (actualResult.equals(expectedResult)) {
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }

    @RepeatedTest(10)
    @DisplayName("Test of sumToN")
    void testSumToN() {
        int n = random.nextInt(1, 100);
        int result = sumToN(n);
        if (result == n * (n + 1) / 2) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "getEvenInRange-data.csv", delimiter = ',', numLinesToSkip = 1)
    @DisplayName("Test of method getEvenInRange")
    void testGetEvenInRange(int start, int end, String expectedResult) {
        String actualResult = getEvenInRange(start, end);
        if (actualResult.equals(expectedResult)) {
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }
    //to do test hasBug
    @ParameterizedTest
    @CsvFileSource(resources = "findMax-data.csv", delimiter = ';', numLinesToSkip = 1)
    @DisplayName("Test of method findMax")
    void testFindMax(int [] input, int expectedResult) {
        int actualResult = findMax(input);
        if (actualResult == expectedResult) {
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }
    @Test
    @DisplayName("Test of method reverse")
    void testReverse() {
        String[] input = {"a", "b", "c", "d"};
        String[] expectedResult = {"d", "c", "b", "a"};
        String[] actualResult = FirstHomeWork.reverse(input);
        if (actualResult == expectedResult) {
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }
    //to do test calcAverage
    @ParameterizedTest
    @CsvFileSource(resources = "/removeName-data.csv", delimiter = ';', numLinesToSkip = 1)
    @DisplayName("Test of method removeSpecificName")
    void testRemoveSpecificName(String inputNames, String nameToRemove, String expectedNames) {
        List<String> inputList = inputNames.isEmpty() ? new ArrayList<>()
                : Arrays.asList(inputNames.split(","));
        List<String> expectedList = expectedNames.isEmpty() ? new ArrayList<>()
                : Arrays.asList(expectedNames.split(","));
        List<String> actuaList = removeSpecificName(inputList, nameToRemove);
        if (actuaList.equals(expectedList)) {
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }
}