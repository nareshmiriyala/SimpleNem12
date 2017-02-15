package au.com.redenergy.csv;

import au.com.redenergy.excecption.SimpleNemParserException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test to read from SimpleNem12 CSV File
 */
public class CsvReaderTest {
    private Reader csvReader;

    @Before
    public void setUp() throws Exception {
        File file = getFile();
        csvReader = new CsvReader(file);

    }

    private File getFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource("SimpleNem12.csv").getFile());
    }

    @Test(expected = SimpleNemParserException.class)
    public void shouldThrowExceptionWhenInputFileIsNull() throws Exception {
        csvReader.setFile(null);
        csvReader.readLines();
    }

    @Test
    public void shouldReadFromCsvFile() throws Exception {
        assertNotNull(csvReader.readLines());
        assertEquals(csvReader.readLines().size(), 17);
    }
}