package au.com.redenergy.csv;

import java.util.List;

/**
 * Represents a file Reader
 */
public interface Reader {
    public List<String[]> readLines();
}
