import com.expense_tracker.User;
import com.expense_tracker.db.QueryExecutor;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class QueryExecutorTest {

    private final static String DB_NAME = "UserTest";

    @Test
    public void getUserTest_Exists() {
        Connection mySql = DbTestHelper.prepareTestDb();
        DbTestHelper.insertTestUser(mySql, "Test_User", "pw");

        User fetchedUser = QueryExecutor.getUser("Test_User", DB_NAME);

        if (fetchedUser == null) {
            fail("QueryExecutor.getUser() returned Null instead of expected User");
        }

        String fetchedUsername = fetchedUser.getUsername();
        assertEquals("QueryExecutor.getUser() did not return expected User",
                "Test_User", fetchedUser.getUsername());
    }

    @Test
    public void getUserTest_DoesNotExist() {
        Connection mySql = DbTestHelper.prepareTestDb();
        DbTestHelper.insertTestUser(mySql, "something", "pw");
        User nonUser = QueryExecutor.getUser("!something", DB_NAME);
        assertNull("getUser() did not return null when given a username not in the db", nonUser);
    }

    @Test
    public void updateUserTest_Exists() {
        Connection mySql = DbTestHelper.prepareTestDb();
        DbTestHelper.insertTestUser(mySql, "beforeUpdate", "pw");
        User originalUser = QueryExecutor.getUser("beforeUpdate", DB_NAME);
        originalUser.setUsername("afterUpdate");
        boolean userUpdated = QueryExecutor.updateUser(originalUser, DB_NAME);
        assertTrue("Query Executor did not update the User", userUpdated);
        User updatedUser = QueryExecutor.getUser("afterUpdate", DB_NAME);
        String newName = updatedUser.getUsername();
        assertEquals("updateUser() did not properly update username", newName,"afterUpdate");
    }

    @Test
    public void updateUserTest_Not_Exists() {
        Connection mySql = DbTestHelper.prepareTestDb();
        DbTestHelper.insertTestUser(mySql, "beforeUpdate", "pw");
        User notInDB = new User();
        boolean userUpdated = QueryExecutor.updateUser(notInDB, DB_NAME);
        assertFalse("Query Executor returned true to update user not in db", userUpdated);
    }

}
