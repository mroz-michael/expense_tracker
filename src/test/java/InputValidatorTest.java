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
        assertTrue("String 1234.568 should be recognized as a valid double", isValid);
    }

    @Test
    public void validDouble_Test_Valid_Leading_Decimal() {
        String validInput = ".568";
        boolean isValid = InputValidator.validDouble(validInput);
        assertTrue("String 1234.568 should be recognized as a valid double", isValid);
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

    }

    @Test
    public void validDate_Test_Invalid() {

    }
}
