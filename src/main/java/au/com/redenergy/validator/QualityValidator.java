package au.com.redenergy.validator;

import au.com.redenergy.exception.SimpleNemParserException;
import au.com.redenergy.model.Quality;

import static java.lang.String.format;
import static java.util.Objects.isNull;

/**
 * Validates the Quality Value.
 * Created by nmiriyal on 16/02/2017.
 */
public class QualityValidator implements Validator {
    /**
     * Validate the Quality value.Value should be either 'A' or 'E'
     *
     * @param quality- input quality
     * @return - quality value after validation.
     * @throws SimpleNemParserException
     */
    @Override
    public String validate(String quality) throws SimpleNemParserException {
        if (isNull(quality)) {
            throw new SimpleNemParserException("Quality is null");
        }
        try {
            Quality.valueOf(quality);
        } catch (IllegalArgumentException e) {
            throw new SimpleNemParserException(format("Quality value '%s' is not valid,value should be either 'A' or 'E'", quality));
        }
        return quality;
    }
}
