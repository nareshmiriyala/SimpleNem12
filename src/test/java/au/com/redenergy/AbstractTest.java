package au.com.redenergy;

import java.io.File;

/**
 * Created by nmiriyal on 15/02/2017.
 */
public abstract class AbstractTest {
    protected File getFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource("SimpleNem12.csv").getFile());
    }
}
