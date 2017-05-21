import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Iterator;

class DynamicTransformationsTest {
    @TestFactory
    Iterator<DynamicTest> createMutantTests() throws Exception{

        return (new DataGenerator()).getTestList();
    }
}