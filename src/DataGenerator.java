import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class generates data
 */
class DataGenerator {

    static final String METHOD_FFT = "fft";
    static final String METHOD_CONVOLVE = "convolve";
    static final String METHOD_CCONVOLVE = "cconvolve";
    static final String METHOD_IFFT = "ifft";

    private static final Map<String, String> methodPaths;
    static
    {
        methodPaths = new HashMap<>();
        methodPaths.put(DataGenerator.METHOD_FFT, "Complex_fft(Complex)");
        methodPaths.put(DataGenerator.METHOD_IFFT, "Complex_ifft(Complex)");
        methodPaths.put(DataGenerator.METHOD_CONVOLVE, "Complex_cconvolve(Complex,Complex)");
        methodPaths.put(DataGenerator.METHOD_CCONVOLVE, "Complex_convolve(Complex,Complex)");
    }

    private List<DynamicTest> testList;

    public List<DynamicTest> getTestList() throws Exception
    {
        this.testList = new ArrayList<>();
        this.traverseMutantClasses(DataGenerator.METHOD_FFT);


        return this.testList;
    }

    public Executable getSingleMutantTest(String singleMutant) throws Exception
    {
        this.testList = new ArrayList<>();

        return this.getSingleMutantClassExec(DataGenerator.METHOD_FFT, singleMutant);
    }

    /**
     * Adds FFT test to stream.
     */
    private void addFFTToStream(File classFile) throws IOException
    {
        Complex[] input = this.getRandomComplex();
        this.testList.add(DynamicTest.dynamicTest(classFile.getParentFile().getName(), new fftTest(this, classFile.getAbsolutePath(), input, FFTOracle.fft(input))));
    }



    /**
     * Returns a random complex array for testing.
     *
     * @return Complex[]
     */
    private Complex[] getRandomComplex()
    {
        //int dimensions = ThreadLocalRandom.current().nextInt(1, 4);
        int dimensions = 2;
        Complex[] complexes = new Complex[dimensions];
        for (int i = 0; i < dimensions; i++) {
            complexes[i] = new Complex( i, 0 );
            complexes[i] = new Complex( -2 * Math.random() + 1, 0 );
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
                switch (methodToTest){
                    case DataGenerator.METHOD_FFT:
                        this.addFFTToStream(child.listFiles()[0]);
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
        Complex[] input = this.getRandomComplex();
        return new fftTest(this, classFile.getAbsolutePath(), input, FFTOracle.fft(input));
    }
}
