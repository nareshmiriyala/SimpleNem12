package au.com.redenergy.validator;

import au.com.redenergy.exception.SimpleNemParserException;

import static java.lang.String.format;
import static java.util.Objects.isNull;

/**
 * Validates the NMI length.
 * Created by nmiriyal on 16/02/2017.
 */
public class NmiValidator implements Validator {
    /**
     * validate the length of NMI .NMI length should be 10.
     *
     * @param nmi - input nmi
     * @return
     * @throws SimpleNemParserException
     */
    @Override
    public String validate(String nmi) throws SimpleNemParserException {
        if (isNull(nmi)) {
            throw new SimpleNemParserException("Input NMI is null");
        }
        if (nmi.length() != 10) {
            throw new SimpleNemParserException(format("NMI '%s' length should be 10", nmi));
        }
        return nmi;
    }
}
