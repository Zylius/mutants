import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Zylius on 2017-05-21.
 */
abstract public class ComplexTest  implements Executable {

    String classFileToTest;
    int runTime = 0;
    private String[] unsolvableCases = {"AOIS_59", "AOIS_60", "AOIU_9"};
    private Object expectedOutput;
    protected DataGenerator dataGenerator;

    public ComplexTest(Object expectedOutput, String classFileToTest, int runTime, DataGenerator dataGenerator)
    {
        this.dataGenerator = dataGenerator;
        this.classFileToTest = classFileToTest;
        this.expectedOutput = expectedOutput;
        this.runTime = runTime;
    }

    abstract public Callable<Object> getCallable() throws Throwable;

    public void execute() throws Throwable {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        if((Arrays.asList(this.unsolvableCases).contains(new File(this.classFileToTest).getParentFile().getName()))) {
            fail("This case is not solvable, mutation is invalid");
        }

        Future<Object> future = executor.submit(this.getCallable());
        try {
            Object value = future.get(1, TimeUnit.SECONDS);
            if (value instanceof Complex[]) {
                this.compareComplexes(value);
            } else if (value instanceof Throwable) {
                this.compareComplexes(value);
            }
        } catch (TimeoutException timeout) {
            this.compareComplexes(timeout);
        }
        this.getNextTest().execute();
    }

    abstract public Executable getNextTest() throws IOException;

    private void compareComplexes(Object actual)
    {
        try {
            if (this.expectedOutput instanceof Throwable) {
                assertEquals(
                        ((Throwable) this.expectedOutput).getClass().toString(),
                        actual.getClass().toString()
                );
                return;
            }


            if (!(actual instanceof Complex[])) {
                fail("Exception thrown by the mutation but not by the oracle. " + ((Throwable)actual).getClass().getName());
            }
            assertNotNull(actual);

            Complex[] actualComplexes = (Complex[])actual;
            Complex[] expectedComplexes = (Complex[])this.expectedOutput;

            assertEquals(expectedComplexes.length, actualComplexes.length);

            for (Complex expectedComplex : expectedComplexes) {
                assertNotNull(actualComplexes[Arrays.asList(expectedComplexes).indexOf(expectedComplex)]);
                assertEquals(expectedComplex.Im(), actualComplexes[Arrays.asList(expectedComplexes).indexOf(expectedComplex)].Im());
                assertEquals(expectedComplex.Re(), actualComplexes[Arrays.asList(expectedComplexes).indexOf(expectedComplex)].Re());
            }
        } catch (AssertionError ex) {
            System.out.println("Needed " + this.runTime + " times to fail");
            throw ex;
        }
    }
}
