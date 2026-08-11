import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.example.BaseJavaMethods.*;
import static org.example.BaseJavaMethods.calcAverage;
import static org.example.BaseJavaMethods.findMax;
import static org.example.BaseJavaMethods.getEvenInRange;
import static org.example.BaseJavaMethods.hasBug;
import static org.example.BaseJavaMethods.removeSpecificName;
import static org.example.BaseJavaMethods.reverse;

public class GradleJunitSecondTaskTest {
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
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/getEvenInRange-data.csv", delimiter = ',', numLinesToSkip = 1)
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
    @Test
    @DisplayName("Test of method findBug")
    void testFindBug() {
        String[] withBug = {"Hello", "bug", "World"};
        String[] withoutBug = {"Hello", "Mother", "World"};
        boolean result = hasBug(withBug);
        boolean result2 = hasBug(withoutBug);
        if (result && !result2) {
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/find-max-data.csv", delimiter = ';', numLinesToSkip = 1)
    @DisplayName("Test of method findMax")
    void testFindMax(String input, int expectedResult) {
        String [] inputString = input.split(",");
        int [] inputList = new int[inputString.length];
        for (int i = 0; i < inputString.length; i++) {
            inputList[i] = Integer.parseInt(inputString[i].trim());
        }
        int actualResult = findMax(inputList);
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
        String[] actualResult = reverse(input);
        if (actualResult == expectedResult) {
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }
    //to do test calcAverage
    @RepeatedTest(5)
    @DisplayName("Test of method calcAvarage")
    void testCalcAverage() {
        int size = random.nextInt(1, 11);
        List<Integer> list = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < size; i++) {
            int number = random.nextInt(1, 100);
            list.add(number);
            sum += number;
        }
        int actualResult = calcAverage(list);
        int expectedResult = sum / size;
        if (actualResult == expectedResult) {
            System.out.println(pass);
        } else {
            System.out.println(fail);
        }
    }

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
