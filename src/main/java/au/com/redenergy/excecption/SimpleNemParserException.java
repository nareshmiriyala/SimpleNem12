package au.com.redenergy.excecption;

/**
 * Exception thrown when parsing Nem12 file.Its checked exception.
 */
public class SimpleNemParserException extends Exception {
    public SimpleNemParserException(String message) {
        super(message);
    }

    public SimpleNemParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
