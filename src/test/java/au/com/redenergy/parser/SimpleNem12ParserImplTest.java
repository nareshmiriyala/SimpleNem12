package au.com.redenergy.parser;

import au.com.redenergy.AbstractTest;
import au.com.redenergy.csv.CsvReader;
import au.com.redenergy.excecption.SimpleNemParserException;
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

    @Test(expected = SimpleNemParserException.class)
    public void shouldThrowExceptionWhenNotStartingWith100() throws Exception {
        simpleNem12Parser.parseSimpleNem12(getFile("Nem12_NotStartingWith100.csv"));
    }

    @Test(expected = SimpleNemParserException.class)
    public void shouldThrowExceptionWhenNotEndingWith900() throws Exception {
        simpleNem12Parser.parseSimpleNem12(getFile("Nem12_NotEndingWith900.csv"));
    }

    @Test(expected = SimpleNemParserException.class)
    public void shouldThrowExceptionWhenNoMeterRecords() throws Exception {
        simpleNem12Parser.parseSimpleNem12(getFile("emptyNem12.csv"));
    }
}