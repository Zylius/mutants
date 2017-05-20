import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class fftTest implements Executable {

    private String classFileToTest;

    private Complex[] input;

    private DataGenerator dataGenerator;

    private Complex[] expectedOutput;

    fftTest(DataGenerator dataGenerator, String classFileToTest, Complex[] input, Complex[] expectedOutput) {
        this.classFileToTest = classFileToTest;
        this.input = input;
        this.dataGenerator = dataGenerator;
        this.expectedOutput = expectedOutput;
    }

    @Override
    public void execute() throws Throwable {
        Class classToTest = (new TestClassLoader()).loadClass(FFT.class.getName(), new File(this.classFileToTest));

        Method method = classToTest.getDeclaredMethod(DataGenerator.METHOD_FFT, Complex[].class);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Object> callable = new Callable<Object>() {
            @Override
            public Object call() {
                try {
                    return method.invoke(null, (Object) fftTest.this.input);
                } catch (Throwable error) {
                    return error;
                }
            }
        };
        Future<Object> future = executor.submit(callable);
        try {
            Object value = future.get(1, TimeUnit.SECONDS);
            if (value instanceof Complex[]) {
                assertArrayEquals(this.expectedOutput, (Complex[]) value);
            } else if (value instanceof Throwable) {
                fail(((Throwable)value).getMessage());
            }
        } catch (TimeoutException timeout) {
            fail("FFT method timeout out for 1 second. Can safely assert it's bad.");
        }

    }
}
