package au.com.redenergy.csv;

import au.com.redenergy.AbstractTest;
import au.com.redenergy.exception.SimpleNemParserException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test to read from SimpleNem12 CSV File
 */
public class CsvReaderTest extends AbstractTest {
    private Reader csvReader;

    @Before
    public void setUp() throws Exception {
        csvReader = new CsvReader();
        csvReader.setFile(getFile());

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