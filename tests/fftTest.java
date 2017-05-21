import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class fftTest extends ComplexTest{
    private Complex[] input;

    fftTest(int runTime, DataGenerator dataGenerator, String classFileToTest, Complex[] input, Object expectedOutput) {
        super(expectedOutput, classFileToTest, runTime, dataGenerator);
        this.input = input;
    }

    public Callable<Object> getCallable() throws Throwable {
        Class classToTest = (new TestClassLoader()).loadClass(FFT.class.getName(), new File(this.classFileToTest));
        Method method = classToTest.getDeclaredMethod(DataGenerator.METHOD_FFT, Complex[].class);
        return () -> {
            try {
                return method.invoke(null, (Object) fftTest.this.input);
            } catch (InvocationTargetException error) {
                return error.getTargetException();
            } catch (IllegalAccessException exception) {
                return exception;
            }
        };
    }

    @Override
    public Executable getNextTest() throws IOException {
        return this.dataGenerator.getFFTTest(new File(classFileToTest), runTime+1);
    }
}
