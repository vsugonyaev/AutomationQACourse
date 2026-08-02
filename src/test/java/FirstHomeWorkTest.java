package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.example.FirstHomeWork.*;
import static org.junit.jupiter.api.Assertions.*;

class FirstHomeWorkTest {

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
        Random random = new Random();
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
        Random random = new Random();
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
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            testData.add(random.nextInt(0, 100));
        }
        return testData.stream().toList();
    }
}