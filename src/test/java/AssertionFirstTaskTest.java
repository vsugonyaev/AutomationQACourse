import org.example.AssertionMethods;
import org.example.AssertionMethods.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.BaseJavaMethods.isEven;

public class AssertionFirstTaskTest {
    private static final Random random = new Random();
    private static final String theme = "Theme3.1";
    @Test
    @DisplayName("Test of method Calculator")
    @Tag(theme)
    void testAdd() {
        int n1 = random.nextInt(100);
        int n2 = random.nextInt(100);
        int actualResult = Calculator.add(n1, n2);
        int expectedResult = n1 + n2;
        // AssertJ проверяет, что результат равен 5
        assertThat(actualResult)
                .as("Проверка сложения")
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/getDivisor-data.csv", delimiter = ';', numLinesToSkip = 1)
    @DisplayName("Test of method getDivisor")
    @Tag(theme)
    void testGetDivisor(int number, String divisors) {
        List<Integer> expected = Arrays.stream(divisors.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Integer> actual = AssertionMethods.getDivisors(number);

        assertThat(actual)
                .as("Делители числа %d", number)
                .containsExactlyElementsOf(expected);
    }

    @Test
    @DisplayName("Another test isEven")
    @Tag(theme)
    void testIsEven() {
        assertThat(isEven(2))
                .as("2 должно быть чётным")
                .isTrue();

        assertThat(isEven(3))
                .as("3 должно быть нечётным")
                .isFalse();

        assertThat(isEven(0))
                .as("0 должно быть чётным")
                .isTrue();

        assertThat(isEven(-4))
                .as("-4 должно быть чётным")
                .isTrue();
    }
}
