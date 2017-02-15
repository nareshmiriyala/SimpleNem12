package au.com.redenergy.validator;

import au.com.redenergy.exception.SimpleNemParserException;

/**
 * Created by nmiriyal on 16/02/2017.
 */
public interface Validator {
    String validate(String record) throws SimpleNemParserException;
}
