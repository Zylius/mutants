import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class generates data
 */
class DataGenerator {

    static final String METHOD_FFT = "fft";
    static final String METHOD_CONVOLVE = "convolve";
    static final String METHOD_CCONVOLVE = "cconvolve";
    static final String METHOD_IFFT = "ifft";

    private int[] edgeCases = {0, 3};
    public Random random = new Random();
    private static final Map<String, String> methodPaths;
    static
    {
        methodPaths = new HashMap<>();
        methodPaths.put(DataGenerator.METHOD_FFT, "Complex_fft(Complex)");
        methodPaths.put(DataGenerator.METHOD_IFFT, "Complex_ifft(Complex)");
        methodPaths.put(DataGenerator.METHOD_CONVOLVE, "Complex_cconvolve(Complex,Complex)");
        methodPaths.put(DataGenerator.METHOD_CCONVOLVE, "Complex_convolve(Complex,Complex)");
    }

    private ArrayList<DynamicTest> testList;

    public Iterator<DynamicTest> getTestList() throws Exception
    {
        this.testList = new ArrayList<DynamicTest>();
        this.traverseMutantClasses(DataGenerator.METHOD_FFT);
        this.traverseMutantClasses(DataGenerator.METHOD_IFFT);

        return this.testList.iterator();
    }

    public Executable getSingleMutantTest(String singleMutant) throws Exception
    {
        return this.getSingleMutantClassExec(DataGenerator.METHOD_FFT, singleMutant);
    }

    /**
     * Returns FFT test to stream.
     */
    public Executable getFFTTest(File classFile, Integer runTime) throws IOException
    {
        Complex[] input = this.getRandomComplex(this.getDimensions(runTime));
        Object expectedResult;
        try {
            expectedResult = FFTOracle.fft(input);
        } catch (Throwable throwable) {
            // Means this should fail.
            expectedResult = throwable;
        }
        return new fftTest(runTime, this, classFile.getAbsolutePath(), input, expectedResult);
    }

    /**
     * Returns IFFT test to stream.
     */
    public Executable getIFFTTest(File classFile, Integer runTime) throws IOException
    {
        Complex[] input = this.getRandomComplex(this.getDimensions(runTime));
        Object expectedResult;
        try {
            expectedResult = FFTOracle.ifft(input);
        } catch (Throwable throwable) {
            // Means this should fail.
            expectedResult = throwable;
        }
        return new ifftTest(runTime, this, classFile.getAbsolutePath(), input, expectedResult);
    }

    private int getDimensions(Integer runTime)
    {
        if (runTime < 3){
            return this.edgeCases[runTime - 1];
        }

        return (int)Math.pow(2, runTime - 2);
    }

    /**
     * Returns a random complex array for testing.
     *
     * @return Complex[]
     */
    private Complex[] getRandomComplex(Integer dimensions)
    {
        dimensions = dimensions == null ? 2 : dimensions;
        Complex[] complexes = new Complex[dimensions];
        for (int i = 0; i < dimensions; i++) {
            complexes[i] = new Complex(random.nextDouble() * dimensions, random.nextDouble() );
            complexes[i] = new Complex( -2 * random.nextDouble(), random.nextDouble() );
        }

        return complexes;
    }

    /**
     * Returns mutant classes to be tested.
     *
     */
    private void traverseMutantClasses(String methodToTest) throws ClassNotFoundException, IOException
    {
        File dir = new File("traditional_mutants/" + DataGenerator.methodPaths.get(methodToTest));
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                File classFile = child.listFiles()[0];
                switch (methodToTest){
                    case DataGenerator.METHOD_FFT:
                        this.testList.add(
                                DynamicTest.dynamicTest(classFile.getParentFile().getName(), this.getFFTTest(classFile, 1))
                        );
                        break;
                    case DataGenerator.METHOD_IFFT:
                        this.testList.add(
                                DynamicTest.dynamicTest(classFile.getParentFile().getName(), this.getIFFTTest(classFile, 1))
                        );
                        break;
                }
            }
        }
    }

    /**
     * Returns mutant classes to be tested.
     *
     */
    private Executable getSingleMutantClassExec(String methodToTest, String mutantToTest) throws ClassNotFoundException, IOException
    {
        File dir = new File("traditional_mutants/" + DataGenerator.methodPaths.get(methodToTest) + '/' + mutantToTest);
        File classFile = dir.listFiles()[0];
        Complex[] input = this.getRandomComplex(1);
        switch (methodToTest){
            case DataGenerator.METHOD_FFT:
                return new fftTest(1, this, classFile.getAbsolutePath(), input, FFTOracle.fft(input));
            default:
                return new ifftTest(1, this, classFile.getAbsolutePath(), input, FFTOracle.ifft(input));

        }
    }
}
