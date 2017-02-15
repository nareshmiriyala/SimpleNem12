package au.com.redenergy.validator;

import au.com.redenergy.exception.SimpleNemParserException;
import au.com.redenergy.model.EnergyUnit;

import static java.lang.String.format;
import static java.util.Objects.isNull;

/**
 * Validate the EnergyUnit value in the csv file
 * Created by nmiriyal on 16/02/2017.
 */
public class EnergyUnitValidator implements Validator {
    /**
     * Validate the Energy Unit of the meter.Value should be KWH
     *
     * @param energyUnit - energyUnit value in csv file
     * @return - energyUnit value if its valid
     * @throws SimpleNemParserException
     */
    @Override
    public String validate(String energyUnit) throws SimpleNemParserException {
        if (isNull(energyUnit)) {
            throw new SimpleNemParserException("EnergyUnit is null");
        }
        try {
            EnergyUnit.valueOf(energyUnit);
        } catch (IllegalArgumentException e) {
            throw new SimpleNemParserException(format("EnergyUnit value '%s' is invalid,value should be 'KWH'", energyUnit));
        }
        return energyUnit;
    }
}
