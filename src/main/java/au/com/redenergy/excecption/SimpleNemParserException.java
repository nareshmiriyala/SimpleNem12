package au.com.redenergy.excecption;

/**
 * Exception thrown when parsing Nem12 file.
 */
public class SimpleNemParserException extends Exception {
    public SimpleNemParserException(String message) {
        super(message);
    }

    public SimpleNemParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
