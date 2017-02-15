package au.com.redenergy.parser;

import au.com.redenergy.csv.Reader;
import au.com.redenergy.exception.SimpleNemParserException;
import au.com.redenergy.exception.SimpleParserRuntimeException;
import au.com.redenergy.model.EnergyUnit;
import au.com.redenergy.model.MeterRead;
import au.com.redenergy.model.MeterVolume;
import au.com.redenergy.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static au.com.redenergy.model.Quality.valueOf;
import static java.lang.String.format;
import static java.util.Objects.isNull;

/**
 * Nem12 parser implementation which reads nem12 file and returns the collection of meterreads.
 */
public class SimpleNem12ParserImpl implements SimpleNem12Parser {
    private final Reader csvReader;
    private final Logger logger = LoggerFactory.getLogger(SimpleNem12Parser.class);
    private Validator nmiValidator;
    private Validator qualityValidator;
    private Validator energyUnitValidator;

    public SimpleNem12ParserImpl(Reader csvReader, Validator inNmiValidator, Validator inQualityValidator, Validator energyUnitValidator) {
        this.csvReader = csvReader;
        nmiValidator = inNmiValidator;
        qualityValidator = inQualityValidator;
        this.energyUnitValidator = energyUnitValidator;
    }

    /**
     * Parses Simple NEM12 file.
     *
     * @param simpleNem12File file in Simple NEM12 format
     * @return Collection of <code>MeterRead</code> that represents the data in the given file.
     */
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
        validateDataInTheCsv(records);

        records.forEach(record -> {
            try {
                parserLine(record, meterReads);
            } catch (SimpleNemParserException e) {
                logger.error("Exception thrown when parsing the record {}", record, e);
                throw new SimpleParserRuntimeException(e.getMessage());
            }
        });
        return meterReads;
    }

    /**
     * validate data in the csv
     *
     * @param records
     * @throws SimpleNemParserException
     */
    private void validateDataInTheCsv(List<String[]> records) throws SimpleNemParserException {
        startingRecordShouldBe100(records);
        endingRecordShouldBe900(records);

    }

    /**
     * verify of the end of the csv is 900
     *
     * @param records - input the csv data
     * @throws SimpleNemParserException
     */
    private void endingRecordShouldBe900(List<String[]> records) throws SimpleNemParserException {
        Optional<String[]> last = records.stream().reduce((first, second) -> second);
        compareValues(last, "900", "last");
    }

    /**
     * verify if the start of csv is 100
     *
     * @param records - input the csv data
     * @throws SimpleNemParserException
     */
    private void startingRecordShouldBe100(List<String[]> records) throws SimpleNemParserException {
        Optional<String[]> first = records.stream().findFirst();
        compareValues(first, "100", "first");
    }

    /**
     * Compare values provided as input
     *
     * @param line
     * @param value
     * @throws SimpleNemParserException
     */
    private void compareValues(Optional<String[]> line, String value, String logMessage) throws SimpleNemParserException {
        if (line.isPresent() && !value.equals(line.get()[0])) {
            throw new SimpleNemParserException(format("RecordType %s must be the %s line in the file", value, logMessage));
        }
    }

    /**
     * Parse the records and read meter read data
     *
     * @param record     - input records
     * @param meterReads - list to add meter reads
     * @throws SimpleNemParserException
     */
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

    /**
     * Add meter read volume data to meter reads
     *
     * @param recordType  - input record
     * @param meterReads  - list of meter reads
     * @param firstColumn - first column of the record
     * @throws SimpleNemParserException
     */
    private void addMeterVolumeToMeterReadsIfStartWith300(String[] recordType, List<MeterRead> meterReads, String firstColumn) throws SimpleNemParserException {
        if ("300".equals(firstColumn)) {
            //Get the last element from meterreads list and add the meter volume
            MeterRead meterRead = meterReads.get(meterReads.size() - 1);
            MeterVolume meterVolume = new MeterVolume(BigDecimal.valueOf(Double.parseDouble(recordType[2])), valueOf(qualityValidator.validate(recordType[3])));
            meterRead.appendVolume(parseDate(recordType[1]), meterVolume);

        }
    }

    /**
     * Add meter read data to meterreads list
     *
     * @param record      - input records
     * @param meterReads  - list of meter reads
     * @param firstColumn - first column of the record.
     * @throws SimpleNemParserException
     */
    private void addMeterReadBlockIfStartsWith200(String[] record, List<MeterRead> meterReads, String firstColumn) throws SimpleNemParserException {
        if ("200".equals(firstColumn)) {
            MeterRead meterRead = createMeterRead(record);
            meterReads.add(meterRead);
        }
    }

    /**
     * Parse the meter volume date to local date
     *
     * @param date - input date
     * @return - LocalDate object
     * @throws SimpleNemParserException
     */
    private LocalDate parseDate(String date) throws SimpleNemParserException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            formatter = formatter.withLocale(Locale.ENGLISH);
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new SimpleNemParserException(format("Not able to parse date %s", date));
        }


    }

    /**
     * Create Meter read record
     *
     * @param record
     * @return - created meter read
     * @throws SimpleNemParserException
     */
    private MeterRead createMeterRead(String[] record) throws SimpleNemParserException {
        MeterRead meterRead = new MeterRead();
        meterRead.setNmi(nmiValidator.validate(record[1]));
        meterRead.setEnergyUnit(EnergyUnit.valueOf(energyUnitValidator.validate(record[2])));
        return meterRead;
    }

    /**
     * validate if the input csv contains any records of not.
     *
     * @param records
     * @throws SimpleNemParserException
     */
    private void validateIfNem12FileContainsMeterReads(List<String[]> records) throws SimpleNemParserException {
        if (isNull(records) || records.size() == 0) {
            throw new SimpleNemParserException("SimpleNem12 file doesn't have any meter records");
        }
    }

    /**
     * validate if csv is null or not.
     *
     * @throws SimpleNemParserException
     */
    private void validateCsvFile() throws SimpleNemParserException {
        if (isNull(csvReader)) {
            throw new SimpleNemParserException("CsvReader can't be null");
        }
    }
}
