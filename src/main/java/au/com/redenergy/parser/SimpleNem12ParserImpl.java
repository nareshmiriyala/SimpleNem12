package au.com.redenergy.parser;

import au.com.redenergy.csv.Reader;
import au.com.redenergy.excecption.SimpleNemParserException;
import au.com.redenergy.model.MeterRead;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Nem12 parser implementation which reads nem12 file and returns the collection of meterreads.
 */
public class SimpleNem12ParserImpl implements SimpleNem12Parser {
    private Reader csvReader;

    public SimpleNem12ParserImpl(Reader csvReader) {
        this.csvReader = csvReader;
    }

    @Override
    public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) throws SimpleNemParserException {
        if(isNull(csvReader)){
            throw new SimpleNemParserException("CsvReader can't be null");
        }
        List<String[]> records = csvReader.readLines();
        return null;
    }
}
