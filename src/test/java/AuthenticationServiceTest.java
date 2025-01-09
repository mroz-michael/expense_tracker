import com.expense_tracker.User;
import com.expense_tracker.services.AuthenticationService;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class AuthenticationServiceTest {

    private final static boolean IS_TEST = true;

    @Test
    public void login_Test_Valid_User() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "myUser", "password1234");
        User myUser = AuthenticationService.login("myUser", "password1234", IS_TEST);
        assertNotNull("login() returned null when given valid credentials", myUser);
    }

    @Test
    public void login_Test_Invalid_Username() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "myUser", "password1234");
        User notMyUser = AuthenticationService.login("password1234", "password1234", IS_TEST);
        assertNull("login() didn't return null when given invalid username", notMyUser);
    }

    @Test
    public void login_Test_Invalid_Password() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "myUser", "password1234");
        User notMyUser = AuthenticationService.login("myUser", "Password1234", IS_TEST);
        assertNull("login() didn't null when given invalid password", notMyUser);
    }

    @Test
    public void generateHash_Test() {
        String txt = "password1234";
        String encrypted = AuthenticationService.generateHash(txt);
        assertNotEquals("generateHash returned plaintxt password","password1234", encrypted);
    }

    @Test
    public void validatePassword_Test_Valid_Password() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "has a valid pw", "valid");
        boolean pwValid = AuthenticationService.validatePassword("has a valid pw", "valid", IS_TEST);
        assertTrue("validatePassword returned false for valid username/pw combination", pwValid);
    }

    @Test
    public void validatePassword_Test_Invalid_Password() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "has a valid pw", "valid");
        boolean pwValid = AuthenticationService.validatePassword("has a valid pw", "invalid", IS_TEST);
        assertFalse("validatePassword returned true for invalid name/pw combination", pwValid);
    }


}
