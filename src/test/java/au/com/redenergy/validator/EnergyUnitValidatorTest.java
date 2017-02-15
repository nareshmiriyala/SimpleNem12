package au.com.redenergy.validator;

import au.com.redenergy.exception.SimpleNemParserException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nmiriyal on 16/02/2017.
 */
public class EnergyUnitValidatorTest {
    private static final String KWH = "KWH";
    private Validator energyUnitValidator;

    @Before
    public void setUp() throws Exception {
        energyUnitValidator = new EnergyUnitValidator();

    }

    @Test(expected = SimpleNemParserException.class)
    public void testInvalidEnergyUnit() throws Exception {

        energyUnitValidator.validate("WWW");

    }
    @Test
    public void testValidEnergyUnit() throws Exception {

        assertEquals(KWH,energyUnitValidator.validate("KWH"));

    }
}