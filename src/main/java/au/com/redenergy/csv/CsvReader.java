package au.com.redenergy.csv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.redenergy.excecption.SimpleNemParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Reader to read from CSV file.
 */
public class CsvReader implements Reader {
    private File csvFile;
    private Logger logger = LoggerFactory.getLogger(CsvReader.class);

    public CsvReader(File file) {
        csvFile = file;
    }

    /**
     * Read all lines from csv.
     *
     * @return all lines as String array.
     */
    @Override
    public List<String[]> readLines() throws SimpleNemParserException {

        if (isNull(csvFile)) {
            throw new SimpleNemParserException("Input csv file cant be null");
        }
        logger.info("Started reading the csv file '{}'", csvFile.getName());
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            return reader.readAll();
        } catch (IOException e) {
            throw new SimpleNemParserException("Exception reading the csvfile ", e);
        }
    }
}
