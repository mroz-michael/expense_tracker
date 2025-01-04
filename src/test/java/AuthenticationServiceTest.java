import com.expense_tracker.services.AuthenticationService;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class AuthenticationServiceTest {

    private final static boolean IS_TEST = true;

    @Test
    public void generateHash_Test() {
        String txt = "password1234";
        String encrypted = AuthenticationService.generateHash(txt);
        assertNotEquals("Authentication Server didn't return hashed String","password1234", encrypted);
    }

    @Test
    public void validatePassword_Test_Valid_Password() {
        Connection mySql = DbTestHelper.prepareUsersTestTable();
        DbTestHelper.insertTestUser(mySql, "has a valid pw", "valid");
        boolean pwValid = AuthenticationService.validatePassword("has a valid pw", "valid", IS_TEST);
        assertTrue("validatePassword returned false for valid username/pw combination", pwValid);
    }

    @Test
    public void validatePassword_Test_Invalid_Password() {
        Connection mySql = DbTestHelper.prepareUsersTestTable();
        DbTestHelper.insertTestUser(mySql, "has a valid pw", "valid");
        boolean pwValid = AuthenticationService.validatePassword("has a valid pw", "invalid", IS_TEST);
        assertFalse("validatePassword returned true for invalid name/pw combination", pwValid);
    }
}
