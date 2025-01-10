import com.expense_tracker.User;
import com.expense_tracker.db.UserQueryExecutor;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class UserQueryExecutorTest {

    private final static boolean IS_TEST = true;

    @Test
    public void getUserTest_Exists() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "Test_User", "plain_password");

        User fetchedUser = UserQueryExecutor.getUser(1, IS_TEST);

        if (fetchedUser == null) {
            fail("UserQueryExecutor.getUser() returned Null instead of expected User");
        }

        String fetchedUsername = fetchedUser.getUsername();
        assertEquals("UserQueryExecutor.getUser() did not return expected User",
                "Test_User", fetchedUsername);
    }

    @Test
    public void getUserTest_DoesNotExist() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "something", "pw");
        User nonUser = UserQueryExecutor.getUser(2, IS_TEST);
        assertNull("getUser() did not return null when given a user not in the db", nonUser);
    }

    @Test
    public void findUserByNameTest_Exists() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "first user", "1234");
        User firstUser = UserQueryExecutor.findUserByName("first user", IS_TEST);
        assertNotNull("findUserByName returned null when given a valid username", firstUser);
        String username = firstUser.getUsername();
        assertEquals("findUserByName did not return user with expected username", "first user", username);
    }

    @Test
    public void findUserByNameTest_Not_Exists() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "a", "1234");
        User nullUser = UserQueryExecutor.findUserByName("b", IS_TEST);
        assertNull("findUserByName did not return null when given username not in db", nullUser);
    }

    @Test
    public void updateUserTest_Exists() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "beforeUpdate", "pw");
        User originalUser = UserQueryExecutor.getUser(1, IS_TEST);
        originalUser.setUsername("afterUpdate");
        boolean userUpdated = UserQueryExecutor.updateUser(originalUser, IS_TEST);
        assertTrue("Query Executor did not update the User", userUpdated);
        User updatedUser = UserQueryExecutor.getUser(1, IS_TEST);
        String newName = updatedUser.getUsername();
        assertEquals("updateUser() did not properly update username", newName,"afterUpdate");
    }

    @Test
    public void updateUserTest_Not_Exists() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "beforeUpdate", "pw");
        User notInDB = new User();
        boolean userUpdated = UserQueryExecutor.updateUser(notInDB, IS_TEST);
        assertFalse("Query Executor returned true to update user not in db", userUpdated);
    }

    @Test
    public void createUserTest() {
        Connection mySql = DbTestHelper.prepareTestTables();
        User newUser = UserQueryExecutor.createUser("firstUser", "pw", "admin", IS_TEST);
        assertEquals("first user created should be given id=1 by db", 1, newUser.getId());
    }

    @Test
    public void deleteUserTest() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "deleteMe", "pw");
        User toDelete = UserQueryExecutor.findUserByName("deleteMe", IS_TEST);
        UserQueryExecutor.deleteUser(toDelete, IS_TEST);
        assertNull("User that should have been deleted was found",
                UserQueryExecutor.findUserByName("deleteMe", IS_TEST));
    }

    //todo implement these:
    @Test
    public void findNumUsers_Test_Not_Empty() {
        Connection mySql = DbTestHelper.prepareTestTables();
        for (int i = 0; i < 3; i++) {
            DbTestHelper.insertTestUser(mySql, "user_" + (i + 1), "password");
        }
        int numUsers = UserQueryExecutor.findNumUsers(IS_TEST);
        assertEquals("findNumUsers did not return expected amount for DB with 3 users", 3, numUsers);
    }


    @Test
    public void findNumUsers_Test_Empty() {
        Connection mySql = DbTestHelper.prepareTestTables();
        int numUsers = UserQueryExecutor.findNumUsers(IS_TEST);
        assertEquals("findNumUsers did not return 0 for empty DB", 0, numUsers);
    }
}