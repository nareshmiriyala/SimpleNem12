package au.com.redenergy.csv;

import au.com.redenergy.excecption.SimpleNemParserException;

import java.util.List;

/**
 * Represents a file Reader
 */
public interface Reader {
    public List<String[]> readLines() throws SimpleNemParserException;
}
