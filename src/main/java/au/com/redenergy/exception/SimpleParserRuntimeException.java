package au.com.redenergy.exception;

/**
 * Exception thrown during runtime.Its unchecked exception.
 */
public class SimpleParserRuntimeException extends RuntimeException {
    public SimpleParserRuntimeException(String message) {
        super(message);
    }

    public SimpleParserRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
