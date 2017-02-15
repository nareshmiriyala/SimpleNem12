package au.com.redenergy.csv;

import au.com.redenergy.excecption.SimpleNemParserException;

import java.io.File;
import java.util.List;

/**
 * Represents a file Reader
 */
public interface Reader {
    /**
     * Set the file to read.
     * @param file
     */
    void setFile(File file);

    /**
     * Read all lines from the file
     * @return
     * @throws SimpleNemParserException
     */
    public List<String[]> readLines() throws SimpleNemParserException;
}
