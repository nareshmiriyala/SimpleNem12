package au.com.redenergy.validator;

import au.com.redenergy.exception.SimpleNemParserException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nmiriyal on 16/02/2017.
 */
public class QualityValidatorTest {
    private Validator qualityValidator;

    @Before
    public void setUp() throws Exception {
        qualityValidator = new QualityValidator();

    }

    @Test(expected = SimpleNemParserException.class)
    public void testInvalidQualityValue() throws Exception {
        qualityValidator.validate("W");

    }

    @Test
    public void testValidQualityValue() throws Exception {
        assertEquals("A", qualityValidator.validate("A"));

    }
}