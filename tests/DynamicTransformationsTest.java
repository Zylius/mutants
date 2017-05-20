import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;

class DynamicTransformationsTest {
    @TestFactory
    List<DynamicTest> createMutantTests() throws Exception{

        return (new DataGenerator()).getTestList();
    }
}