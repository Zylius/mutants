import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;

class DynamicTransformationsTest {
    @TestFactory
    List<DynamicTest> createPointTests() {
        DataGenerator dataGenerator = new DataGenerator();

        return Arrays.asList(
                DynamicTest.dynamicTest(
                        "A Great Test For Transformation",
                        () -> {
                        })
        );
    }
    @Test
    void helloJUnit5() {
        System.out.println("Hello, JUnit 5.");
    }

}