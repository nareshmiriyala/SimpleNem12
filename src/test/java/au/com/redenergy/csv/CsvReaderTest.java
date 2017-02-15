package au.com.redenergy.csv;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test to read from SimpleNem12 CSV File
 */
public class CsvReaderTest {
    private Reader csvReader;

    @Before
    public void setUp() throws Exception {
        csvReader = new CsvReader();

    }

    @Test
    public void shouldReadFromCsvFile() throws Exception {
        assertNotNull(csvReader.readLines());
        assertEquals(csvReader.readLines().size(), 17);
    }
}