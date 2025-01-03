import com.expense_tracker.User;
import com.expense_tracker.db.QueryExecutor;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class QueryExecutorTest {

    private final static String TEST_DB_NAME = "UserTest";

    @Test
    public void getUserTest_Exists() {
        Connection mySql = DbTestHelper.prepareTestDb();
        DbTestHelper.insertTestUser(mySql, "Test_User", "pw");

        User fetchedUser = QueryExecutor.getUser(1, TEST_DB_NAME);

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
        User nonUser = QueryExecutor.getUser(2, TEST_DB_NAME);
        assertNull("getUser() did not return null when given a user not in the db", nonUser);
    }

    @Test
    public void updateUserTest_Exists() {
        Connection mySql = DbTestHelper.prepareTestDb();
        DbTestHelper.insertTestUser(mySql, "beforeUpdate", "pw");
        User originalUser = QueryExecutor.getUser(1, TEST_DB_NAME);
        originalUser.setUsername("afterUpdate");
        boolean userUpdated = QueryExecutor.updateUser(originalUser, TEST_DB_NAME);
        assertTrue("Query Executor did not update the User", userUpdated);
        User updatedUser = QueryExecutor.getUser(1, TEST_DB_NAME);
        String newName = updatedUser.getUsername();
        assertEquals("updateUser() did not properly update username", newName,"afterUpdate");
    }

    @Test
    public void updateUserTest_Not_Exists() {
        Connection mySql = DbTestHelper.prepareTestDb();
        DbTestHelper.insertTestUser(mySql, "beforeUpdate", "pw");
        User notInDB = new User();
        boolean userUpdated = QueryExecutor.updateUser(notInDB, TEST_DB_NAME);
        assertFalse("Query Executor returned true to update user not in db", userUpdated);
    }

    @Test
    public void createUserTest() {
        Connection mySql = DbTestHelper.prepareTestDb();
        User newUser = QueryExecutor.createUser("firstUser", "pw", "admin", TEST_DB_NAME);
        assertEquals("first user created should be given id=1 by db", 1, newUser.getId());
    }

    @Test
    public void deleteUserTest() {
        //todo
    }
}
