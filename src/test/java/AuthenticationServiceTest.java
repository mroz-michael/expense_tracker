import com.expense_tracker.User;
import com.expense_tracker.db.TableNameProvider;
import com.expense_tracker.db.UserQueryExecutor;
import com.expense_tracker.services.AuthenticationService;
import com.expense_tracker.utils.ConfigLoader;

import org.junit.Test;
import org.junit.Before;

import java.io.ObjectInputFilter.Config;
import java.sql.Connection;

import static org.junit.Assert.*;

public class AuthenticationServiceTest {

    private Connection mySql;
    private UserQueryExecutor userExecutor;
    private AuthenticationService authService;

    @Before

    public void setup() {
        try {

            this.mySql = DbTestHelper.prepareTestTables();
            //the DB used for unit-testing has no tableSuffix
            TableNameProvider provider = new TableNameProvider("");
            this.userExecutor = new UserQueryExecutor(mySql, provider);
            this.authService = new AuthenticationService(userExecutor);

        } catch (Exception e) {
            System.out.println("Error preparing test objects " + e.getLocalizedMessage());
        }
    }

    @Test
    public void login_Test_Valid_User() {
        
        DbTestHelper.insertTestUser(mySql, "myUser", "password1234");
        User myUser = authService.login("myUser", "password1234");
        assertNotNull("login() returned null when given valid credentials", myUser);
    }

    @Test
    public void login_Test_Invalid_Username() {
        
        DbTestHelper.insertTestUser(mySql, "myUser", "password1234");
        User notMyUser = authService.login("password1234", "password1234");
        assertNull("login() didn't return null when given invalid username", notMyUser);
    }

    @Test
    public void login_Test_Invalid_Password() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "myUser", "password1234");
        User notMyUser = authService.login("myUser", "Password1234");
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
        DbTestHelper.insertTestUser(mySql, "has a valid pw", "valid");
        boolean pwValid = authService.validatePassword("has a valid pw", "valid");
        assertTrue("validatePassword returned false for valid username/pw combination", pwValid);
    }

    @Test
    public void validatePassword_Test_Invalid_Password() {
        DbTestHelper.insertTestUser(mySql, "has a valid pw", "valid");
        boolean pwValid = authService.validatePassword("has a valid pw", "invalid");
        assertFalse("validatePassword returned true for invalid name/pw combination", pwValid);
    }


}
