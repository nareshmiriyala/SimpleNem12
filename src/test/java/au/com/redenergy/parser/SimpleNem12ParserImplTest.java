package au.com.redenergy.parser;

import au.com.redenergy.AbstractTest;
import au.com.redenergy.csv.CsvReader;
import au.com.redenergy.exception.SimpleNemParserException;
import au.com.redenergy.exception.SimpleParserRuntimeException;
import au.com.redenergy.model.EnergyUnit;
import au.com.redenergy.model.MeterRead;
import au.com.redenergy.model.MeterVolume;
import au.com.redenergy.model.Quality;
import au.com.redenergy.validator.EnergyUnitValidator;
import au.com.redenergy.validator.NmiValidator;
import au.com.redenergy.validator.QualityValidator;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import static au.com.redenergy.model.Quality.A;
import static au.com.redenergy.model.Quality.E;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test for Parsing the Nem12 file.
 */
public class SimpleNem12ParserImplTest extends AbstractTest {
    private SimpleNem12Parser simpleNem12Parser;

    @Before
    public void setUp() throws Exception {
        simpleNem12Parser = new SimpleNem12ParserImpl(new CsvReader(),new NmiValidator(),new QualityValidator(),new EnergyUnitValidator());
    }

    @Test
    public void shouldReadNem12File() throws Exception {
        assertNotNull(simpleNem12Parser.parseSimpleNem12(getFile()));
    }

    @Test
    public void shouldReturnMeterReadsWithValidData() throws Exception {
        Collection<MeterRead> meterReads = simpleNem12Parser.parseSimpleNem12(getFile());
        assertNotNull(meterReads);
        MeterRead firstMeterRead = meterReads.stream().findFirst().get();
        assertNotNull(firstMeterRead);
        assertEquals(firstMeterRead.getNmi(), "6123456789");
        assertEquals(firstMeterRead.getEnergyUnit(), EnergyUnit.KWH);
        SortedMap<LocalDate, MeterVolume> firstMeterReadVolumes = firstMeterRead.getVolumes();
        assertEquals(firstMeterReadVolumes.size(), 7);
        Iterator<Map.Entry<LocalDate, MeterVolume>> iterator = firstMeterReadVolumes.entrySet().iterator();
        assertVolumeData(iterator, 2016, 11, 13, -50.8, A);
        assertVolumeData(iterator, 2016, 11, 14, 23.96, A);
        assertVolumeData(iterator, 2016, 11, 15, 32.0, A);
        assertVolumeData(iterator, 2016, 11, 16, -33, A);
        assertVolumeData(iterator, 2016, 11, 17, 0, A);
        assertVolumeData(iterator, 2016, 11, 18, 0, E);
        assertVolumeData(iterator, 2016, 11, 19, -9, A);
        MeterRead secondMeterRead = meterReads.stream().reduce((first, second) -> second).get();
        assertNotNull(secondMeterRead);
        assertEquals(secondMeterRead.getNmi(), "6987654321");
        assertEquals(secondMeterRead.getEnergyUnit(), EnergyUnit.KWH);
        SortedMap<LocalDate, MeterVolume> secondMeterReadVolumes = secondMeterRead.getVolumes();
        assertEquals(secondMeterReadVolumes.size(), 6);
        Iterator<Map.Entry<LocalDate, MeterVolume>> secondMeterIterator = secondMeterReadVolumes.entrySet().iterator();
        assertVolumeData(secondMeterIterator, 2016, 12, 15, -3.8, A);
        assertVolumeData(secondMeterIterator, 2016, 12, 16, 0, A);
        assertVolumeData(secondMeterIterator, 2016, 12, 17, 3.0, E);
        assertVolumeData(secondMeterIterator, 2016, 12, 18, -12.8, A);
        assertVolumeData(secondMeterIterator, 2016, 12, 19, 23.43, E);
        assertVolumeData(secondMeterIterator, 2016, 12, 21, 4.5, A);
    }

    private void assertVolumeData(Iterator<Map.Entry<LocalDate, MeterVolume>> iterator, int year, int month, int day, double volume, Quality quality) {
        Map.Entry<LocalDate, MeterVolume> next = iterator.next();
        assertEquals(LocalDate.of(year, month, day), next.getKey());

        assertEquals(new MeterVolume(BigDecimal.valueOf(volume), quality), next.getValue());
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

    /**
     * Test for NMI length.NMI length should
     * @throws Exception
     */
    @Test(expected = SimpleParserRuntimeException.class)
    public void testForNmiLength() throws Exception {
        simpleNem12Parser.parseSimpleNem12(getFile("Nem12_NmiLengthLessThan10.csv"));

    }

    /**
     * Test for invalid date in the meter volume data.
     * @throws Exception
     */
    @Test(expected = SimpleParserRuntimeException.class)
    public void testForInvalidDate() throws Exception {
        simpleNem12Parser.parseSimpleNem12(getFile("Nem12_InvalidDate.csv"));

    }
}