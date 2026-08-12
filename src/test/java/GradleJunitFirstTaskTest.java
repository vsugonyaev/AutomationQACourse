
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.BaseJavaMethods.*;

class GradleJunitFirstTaskTest {
    private static final Random random = new Random();
    private static final String theme = "Theme2.1";

    @BeforeEach
    @Tag(theme)
    void start() {
        System.out.println("==============================");
        System.out.println("Test method start");
    }
    @AfterEach
    @Tag(theme)
    void finish()  {
        System.out.println("Test method end");
        System.out.println("==============================");
    }
    @Test
    @DisplayName("Test of method isEven")
    @Tag(theme)
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
    @Tag(theme)
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
    @Tag(theme)
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


}