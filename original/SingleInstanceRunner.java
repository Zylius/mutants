/**
 * Created by Zylius on 2017-05-21.
 *
 * Runs a single instance of test. Used for code coverage.
 */
public class SingleInstanceRunner {
    public static void main (String[] args) throws Throwable {
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.getSingleMutantTest(args[0]).execute();
    }
}
