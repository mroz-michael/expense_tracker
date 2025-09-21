import com.expense_tracker.User;
import com.expense_tracker.db.TableNameProvider;
import com.expense_tracker.db.TransactionQueryExecutor;
import com.expense_tracker.db.UserQueryExecutor;

import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class UserQueryExecutorTest {

    private Connection mySql;
    private UserQueryExecutor userQueryExecutor;

    @Before
    public void setup() {
        try {
            this.mySql = DbTestHelper.prepareTestTables();
             //the DB used for unit-testing has no tableSuffix
            TableNameProvider provider = new TableNameProvider("");
            this.userQueryExecutor = new UserQueryExecutor(mySql, provider);

        } catch (Exception e) {
            System.out.println("Error preparing test objects " + e.getLocalizedMessage());
        }
    }


    @Test
    public void getUserTest_Exists() {
    
        DbTestHelper.insertTestUser(mySql, "Test_User", "plain_password");

        User fetchedUser = userQueryExecutor.getUser(1);

        if (fetchedUser == null) {
            fail("UserQueryExecutor.getUser() returned Null instead of expected User");
        }

        String fetchedUsername = fetchedUser.getUsername();
        assertEquals("UserQueryExecutor.getUser() did not return expected User",
                "Test_User", fetchedUsername);
    }

    @Test
    public void getUserTest_DoesNotExist() {
        
        DbTestHelper.insertTestUser(mySql, "something", "pw");
        User nonUser = userQueryExecutor.getUser(2);
        assertNull("getUser() did not return null when given a user not in the db", nonUser);
    }

    @Test
    public void findUserByNameTest_Exists() {
        
        DbTestHelper.insertTestUser(mySql, "first user", "1234");
        User firstUser = userQueryExecutor.findUserByName("first user");
        assertNotNull("findUserByName returned null when given a valid username", firstUser);
        String username = firstUser.getUsername();
        assertEquals("findUserByName did not return user with expected username", "first user", username);
    }

    @Test
    public void findUserByNameTest_Not_Exists() {
        DbTestHelper.insertTestUser(mySql, "a", "1234");
        User nullUser = userQueryExecutor.findUserByName("b");
        assertNull("findUserByName did not return null when given username not in db", nullUser);
    }

    @Test
    public void updateUserTest_Exists() {
        
        DbTestHelper.insertTestUser(mySql, "beforeUpdate", "pw");
        User originalUser = userQueryExecutor.getUser(1);
        originalUser.setUsername("afterUpdate");
        boolean userUpdated = userQueryExecutor.updateUser(originalUser);
        assertTrue("Query Executor did not update the User", userUpdated);

        User updatedUser = userQueryExecutor.getUser(1);
        String newName = updatedUser.getUsername();
        assertEquals("updateUser() did not properly update username", "afterUpdate", newName);
    }

    @Test
    public void updateUserTest_Not_Exists() {
        
        DbTestHelper.insertTestUser(mySql, "beforeUpdate", "pw");
        User notInDB = new User();
        boolean userUpdated = userQueryExecutor.updateUser(notInDB);
        assertFalse("Query Executor returned true to update user not in db", userUpdated);
    }

    @Test
    public void createUserTest() {
       
        User newUser = userQueryExecutor.createUser("firstUser", "pw", "admin");
        assertEquals("first user created should be given id=1 by db", 1, newUser.getId());
    }

    @Test
    public void deleteUserTest() {
        
        DbTestHelper.insertTestUser(mySql, "deleteMe", "pw");
        User toDelete = userQueryExecutor.findUserByName("deleteMe");
        userQueryExecutor.deleteUser(toDelete);
        assertNull("User that should have been deleted was found",
                userQueryExecutor.findUserByName("deleteMe"));
    }

    @Test
    public void findNumUsers_Test_Not_Empty() {
        
        for (int i = 0; i < 3; i++) {
            DbTestHelper.insertTestUser(mySql, "user_" + (i + 1), "password");
        }
        int numUsers = userQueryExecutor.findNumUsers();
        assertEquals("findNumUsers did not return expected amount for DB with 3 users", 3, numUsers);
    }


    @Test
    public void findNumUsers_Test_Empty() {
        
        int numUsers = userQueryExecutor.findNumUsers();
        assertEquals("findNumUsers did not return 0 for empty DB", 0, numUsers);
    }
}