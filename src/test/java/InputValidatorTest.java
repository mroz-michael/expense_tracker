import com.expense_tracker.utils.InputValidator;
import org.junit.Test;

import static org.junit.Assert.*;

public class InputValidatorTest {

    @Test
    public void validDouble_Test_Valid() {
        String validInput = "1234.568";
        boolean isValid = InputValidator.validDouble(validInput);
        assertTrue("String 1234.568 should be recognized as a valid double", isValid);
    }

    @Test
    public void validDouble_Test_Valid_Negative() {
        String validInput = "-1234.568";
        boolean isValid = InputValidator.validDouble(validInput);
        assertTrue("String -1234.568 should be recognized as a valid double", isValid);
    }

    @Test
    public void validDouble_Test_Valid_Leading_Decimal() {
        String validInput = ".568";
        boolean isValid = InputValidator.validDouble(validInput);
        assertTrue("String .568 should be recognized as a valid double", isValid);
    }


    @Test
    public void validDouble_Test_Invalid() {
        String invalidInput = "1234.12.215";
        boolean isValid = InputValidator.validDouble(invalidInput);
        assertFalse("String with two decimals should not be a valid double", isValid);
    }

    @Test
    public void validDouble_Test_Invalid_Letters() {
        String invalidInput = "13w";
        boolean isValid = InputValidator.validDouble(invalidInput);
        assertFalse("String with letters should not be a valid double", isValid);
    }

    @Test
    public void validDate_Test_Valid() {
        String validDate = "1999 12 31";
        boolean isValid = InputValidator.validDate(validDate);
        assertTrue("String formatted: YYYY MM DD should be validDate", isValid);
    }

    @Test
    public void validDate_Test_Invalid() {
        String invalidDate = "199 01 01";
        boolean isValid = InputValidator.validDate(invalidDate);
        assertFalse("String with 3 digit year should not be valid date", isValid);
    }

    @Test
    public void validInt_Test_Valid() {
        String validInt = "42";
        boolean isValid = InputValidator.validInt(validInt, 41, 43);
        assertTrue("42 should be recognized as valid if range accepted is [41, 43]", isValid);
    }

    @Test
    public void validInt_Test_Valid_Boundary() {
        String validInt = "42";
        boolean isValid = InputValidator.validInt(validInt, 42, 42);
        assertTrue("42 should be recognized as valid if range accepted is [42, 42]", isValid);
    }

    @Test
    public void validInt_Test_Invalid() {
        String invalidInt = "42";
        boolean isValid = InputValidator.validInt(invalidInt, 31, 41);
        assertFalse("42 should be recognized as invalid if range accepted is [31, 41]", isValid);
    }
}
