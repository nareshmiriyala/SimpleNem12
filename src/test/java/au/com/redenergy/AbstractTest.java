package au.com.redenergy;

import java.io.File;

/**
 * Created by nmiriyal on 15/02/2017.
 */
public abstract class AbstractTest {
    protected File getFile(String file) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(file).getFile());
    }

    protected File getFile() {
        return getFile("SimpleNem12.csv");
    }
}
