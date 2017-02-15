package au.com.redenergy.parser;

import au.com.redenergy.AbstractTest;
import au.com.redenergy.csv.CsvReader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Unit test for Parsing the Nem12 file.
 */
public class SimpleNem12ParserImplTest extends AbstractTest {
    private SimpleNem12Parser simpleNem12Parser;

    @Before
    public void setUp() throws Exception {
        simpleNem12Parser = new SimpleNem12ParserImpl(new CsvReader());
    }

    @Test
    public void shouldReadNem12File() throws Exception {
        assertNotNull(simpleNem12Parser.parseSimpleNem12(getFile()));

    }
}