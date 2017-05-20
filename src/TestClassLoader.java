import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

class TestClassLoader extends ClassLoader {


    public Class<?> loadClass(String name, File inputFile) throws ClassNotFoundException {
        if (name.equals("FFT")) {
            try {
                InputStream is  = new FileInputStream(inputFile);
                byte[] buf = new byte[10000];
                int len = is.read(buf);
                return defineClass(name, buf, 0, len);
            } catch (IOException e) {
                throw new ClassNotFoundException("", e);
            }
        }
        return getParent().loadClass(name);
    }
}