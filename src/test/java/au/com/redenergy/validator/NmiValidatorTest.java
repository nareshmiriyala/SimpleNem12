package au.com.redenergy.validator;

import au.com.redenergy.exception.SimpleNemParserException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nmiriyal on 16/02/2017.
 */
public class NmiValidatorTest {

    private Validator nmiValidator;

    @Before
    public void setUp() throws Exception {
        nmiValidator=new NmiValidator();

    }

    @Test(expected = SimpleNemParserException.class)
    public void testWhenNmiIsNull() throws Exception {

        nmiValidator.validate(null);

    }
    @Test(expected = SimpleNemParserException.class)
    public void testWhenNmiLengthNotEqualTo10() throws Exception {

        nmiValidator.validate("12345");

    }
    @Test
    public void testWhenNmiLengthEqualTo10() throws Exception {

        assertEquals("1234567890",nmiValidator.validate("1234567890"));

    }
}