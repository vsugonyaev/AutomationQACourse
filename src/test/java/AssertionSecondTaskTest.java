import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.BaseJavaMethods.*;
import static org.example.BaseJavaMethods.calcAverage;
import static org.example.BaseJavaMethods.findMax;
import static org.example.BaseJavaMethods.getEvenInRange;
import static org.example.BaseJavaMethods.hasBug;
import static org.example.BaseJavaMethods.removeSpecificName;
import static org.example.BaseJavaMethods.reverse;

@Tag("Theme3.2")
public class AssertionSecondTaskTest {
    private static final Random random = new Random();

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

    @RepeatedTest(10)
    @DisplayName("Test of method isPositive")
    void testIsPositive() {
        int n = random.nextInt(-100, 100) ;
        boolean actualResult = isPositive(n);
        boolean expectedResult = n > 0 ;
        assertThat(actualResult)
                .as("isPositive(%d) должен быть %b", n, expectedResult)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/blastOff-data.csv", delimiter = ',', numLinesToSkip = 1)
    @DisplayName("Test of method blastOff")
    void testBlastOff(int start, String expectedResult) {
        String actualResult = blastOff(start);
        assertThat(actualResult)
                .as("blastOff(%d) должен вернуть \"%s\"", start, expectedResult)
                .isEqualTo(expectedResult);
    }

    @RepeatedTest(10)
    @DisplayName("Test of sumToN")
    void testSumToN() {
        int n = random.nextInt(1, 100);
        int actualResult = sumToN(n);
        int expectedResult = n * (n + 1) / 2;

        assertThat(actualResult)
                .as("sumToN(%d) должен быть %d", n, expectedResult)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/getEvenInRange-data.csv", delimiter = ',', numLinesToSkip = 1)
    @DisplayName("Test of method getEvenInRange")
    void testGetEvenInRange(int start, int end, String expectedResult) {
        String actualResult = getEvenInRange(start, end);
        assertThat(actualResult)
                .as("getEvenInRange(%d, %d) должен вернуть \"%s\"", start, end, expectedResult)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/findBug-data.csv", delimiter = ';', numLinesToSkip = 1)
    @DisplayName("Test of method findBug")
    void testFindBug(String words, boolean expectedResult) {
        String[] inputArray = words.split(",");
        boolean actualResult = hasBug(inputArray);

        assertThat(actualResult)
                .as("hasBug(%s) должен быть %b", Arrays.toString(inputArray), expectedResult)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/findMax-data.csv", delimiter = ';', numLinesToSkip = 1)
    @DisplayName("Test of method findMax")
    void testFindMax(String input, int expectedResult) {
        String [] inputString = input.split(",");
        int [] inputList = new int[inputString.length];
        for (int i = 0; i < inputString.length; i++) {
            inputList[i] = Integer.parseInt(inputString[i].trim());
        }
        int actualResult = findMax(inputList);
        assertThat(actualResult)
                .as("findMax(%s) должен быть %d", Arrays.toString(inputList), expectedResult)
                .isEqualTo(expectedResult);
    }
    @Test
    @DisplayName("Test of method reverse")
    void testReverse() {
        String[] input = {"a", "b", "c", "d"};
        String[] expectedResult = {"d", "c", "b", "a"};
        String[] actualResult = reverse(input);
        assertThat(actualResult)
                .as("reverse(%s) должен вернуть %s", Arrays.toString(input), Arrays.toString(expectedResult))
                .containsExactly(expectedResult);
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
        assertThat(actualResult)
                .as("calcAverage(%s) должен быть %d", list, expectedResult)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/removeName-data.csv", delimiter = ';', numLinesToSkip = 1)
    @DisplayName("Test of method removeSpecificName")
    void testRemoveSpecificName(String inputNames, String nameToRemove, String expectedNames) {
        List<String> inputList = inputNames.isEmpty() ? new ArrayList<>()
                : Arrays.asList(inputNames.split(","));
        List<String> expectedList = expectedNames.isEmpty() ? new ArrayList<>()
                : Arrays.asList(expectedNames.split(","));
        List<String> actualList = removeSpecificName(inputList, nameToRemove);
        assertThat(actualList)
                .as("removeSpecificName(%s, \"%s\") должен вернуть %s", inputList, nameToRemove, expectedList)
                .isEqualTo(expectedList);
    }
}
