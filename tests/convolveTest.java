import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class convolveTest extends ComplexTest{

    convolveTest(int runTime, DataGenerator dataGenerator, String classFileToTest, Complex[][] input) {
        super(input, classFileToTest, runTime, dataGenerator);
    }

    public Object getResultFromOracle(Object input)
    {
        Complex[][] complexInput = (Complex[][])input;
        Object expectedResult;
        try {
            expectedResult = FFTOracle.convolve(complexInput[0], complexInput[1]);
        } catch (Throwable throwable) {
            // Means this should fail.
            expectedResult = throwable;
        }
        return expectedResult;
    }

    public Callable<Object> getCallable() throws Throwable {
        Class classToTest = (new TestClassLoader()).loadClass(FFT.class.getName(), new File(this.classFileToTest));
        Method method = classToTest.getDeclaredMethod(DataGenerator.METHOD_CONVOLVE, Complex[].class, Complex[].class);
        Complex[][] complexInput = (Complex[][])input;
        return () -> {
            try {
                return method.invoke(null, (Object)complexInput[0], (Object)complexInput[1]);
            } catch (InvocationTargetException error) {
                return error.getTargetException();
            } catch (IllegalAccessException exception) {
                return exception;
            }
        };
    }

    @Override
    public Executable getNextTest() throws IOException {
        return this.dataGenerator.getConvolveTest(new File(classFileToTest), runTime+1);
    }
}
