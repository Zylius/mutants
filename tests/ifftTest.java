import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

public class ifftTest extends ComplexTest implements Executable {
    ifftTest(int runTime, DataGenerator dataGenerator, String classFileToTest, Complex[] input) {
        super(input, classFileToTest, runTime, dataGenerator);
    }

    public Object getResultFromOracle(Object input)
    {
        Object expectedResult;
        try {
            expectedResult = FFTOracle.ifft((Complex[]) input);
        } catch (Throwable throwable) {
            // Means mutant should fail.
            expectedResult = throwable;
        }
        return expectedResult;
    }

    public Callable<Object> getCallable() throws Throwable {
        Class classToTest = (new TestClassLoader()).loadClass(FFT.class.getName(), new File(this.classFileToTest));

        return () -> {
            try {
                Method method = classToTest.getDeclaredMethod(DataGenerator.METHOD_IFFT, Complex[].class);
                return method.invoke(null, (Object) ifftTest.this.input);
            } catch (InvocationTargetException error) {
                return error.getTargetException();
            } catch (IllegalAccessException exception) {
                return exception;
            }
        };
    }

    @Override
    public Executable getNextTest() throws IOException {
        return this.dataGenerator.getIFFTTest(new File(classFileToTest), runTime+1);
    }
}
