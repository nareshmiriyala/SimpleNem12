package au.com.redenergy.parser;

import au.com.redenergy.csv.Reader;
import au.com.redenergy.excecption.SimpleNemParserException;
import au.com.redenergy.model.EnergyUnit;
import au.com.redenergy.model.MeterRead;
import au.com.redenergy.model.MeterVolume;
import au.com.redenergy.model.Quality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Objects.isNull;

/**
 * Nem12 parser implementation which reads nem12 file and returns the collection of meterreads.
 */
public class SimpleNem12ParserImpl implements SimpleNem12Parser {
    private Reader csvReader;
    private Logger logger = LoggerFactory.getLogger(SimpleNem12Parser.class);

    public SimpleNem12ParserImpl(Reader csvReader) {
        this.csvReader = csvReader;
    }

    @Override
    public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) throws SimpleNemParserException {

        validateCsvFile();
        csvReader.setFile(simpleNem12File);
        List<String[]> records = csvReader.readLines();
        validateIfNem12FileContainsMeterReads(records);
        return parseAndReadMeterReads(records);
    }

    private Collection<MeterRead> parseAndReadMeterReads(List<String[]> records) throws SimpleNemParserException {
        List<MeterRead> meterReads = new ArrayList<>();
        validateDataIntheCsv(records);

        records.forEach(record -> {
            try {
                parserLine(record, meterReads);
            } catch (SimpleNemParserException e) {
                logger.error("Exception thrown when parsing the record {}", record, e);
            }
        });
        return meterReads;
    }

    private void validateDataIntheCsv(List<String[]> records) throws SimpleNemParserException {
        startingRecordShouldBe100(records);
        endingRecordShouldBe900(records);

    }

    private void endingRecordShouldBe900(List<String[]> records) throws SimpleNemParserException {
        Optional<String[]> last = records.stream().reduce((first, second) -> second);
        compareValues(last, "900");
    }

    private void startingRecordShouldBe100(List<String[]> records) throws SimpleNemParserException {
        Optional<String[]> first = records.stream().findFirst();
        compareValues(first, "100");
    }

    private void compareValues(Optional<String[]> first, String value) throws SimpleNemParserException {
        if (first.isPresent() && !value.equals(first.get()[0])) {
            throw new SimpleNemParserException(String.format("RecordType %s must be the first line in the file", value));
        }
    }

    private void parserLine(String[] record, List<MeterRead> meterReads) throws SimpleNemParserException {
        String firstColumn = record[0].trim();
        //if 100 or 900 as start of line then return
        if ("100".equals(firstColumn) || "900".equals(firstColumn)) {
            return;
        }
        //RecordType 200 represents the start of a meter read block
        addMeterReadBlockIfStartsWith200(record, meterReads, firstColumn);
        //Add MeterVolume to meter reads
        addMeterVolumeToMeterReadsIfStartWith300(record, meterReads, firstColumn);
    }

    private void addMeterVolumeToMeterReadsIfStartWith300(String[] recordType, List<MeterRead> meterReads, String firstColumn) {
        if ("300".equals(firstColumn)) {
            //Get the last element from meterreads list and add the meter volume
            MeterRead meterRead = meterReads.get(meterReads.size() - 1);
            MeterVolume meterVolume = new MeterVolume(BigDecimal.valueOf(Double.parseDouble(recordType[2])), Quality.valueOf(recordType[3]));
            meterRead.appendVolume(parseDate(recordType[1]), meterVolume);

        }
    }


    private void addMeterReadBlockIfStartsWith200(String[] record, List<MeterRead> meterReads, String firstColumn) throws SimpleNemParserException {
        if ("200".equals(firstColumn)) {
            MeterRead meterRead = createMeterRead(record);
            meterReads.add(meterRead);
        }
    }

    private LocalDate parseDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        formatter = formatter.withLocale(Locale.ENGLISH);
        return LocalDate.parse(date, formatter);


    }

    private MeterRead createMeterRead(String[] record) throws SimpleNemParserException {
        MeterRead meterRead = new MeterRead();
        meterRead.setNmi(validateNmi(record[1]));
        meterRead.setEnergyUnit(EnergyUnit.valueOf(record[2]));
        return meterRead;
    }

    private String validateNmi(String nmi) throws SimpleNemParserException {
        if (isNull(nmi)) {
            throw new SimpleNemParserException("Input NMI " + nmi + " is invalid");
        }
        if (nmi.length() < 10) {
            throw new SimpleNemParserException(String.format("NMI '%s' length cant be less than 10", nmi));
        }
        return nmi;
    }


    private void validateIfNem12FileContainsMeterReads(List<String[]> records) throws SimpleNemParserException {
        if (isNull(records) || records.size() == 0) {
            throw new SimpleNemParserException("SimpleNem12 file doesn't have any meter records");
        }
    }

    private void validateCsvFile() throws SimpleNemParserException {
        if (isNull(csvReader)) {
            throw new SimpleNemParserException("CsvReader can't be null");
        }
    }
}
