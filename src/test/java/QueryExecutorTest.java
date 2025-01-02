import com.expense_tracker.User;
import com.expense_tracker.db.QueryExecutor;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class QueryExecutorTest {

    @Test
    public void getUserTest_Exists() {
        Connection mySql = DbTestHelper.prepareTestDb();
        DbTestHelper.insertTestUser(mySql, "Test_User", "pw");

        User fetchedUser = QueryExecutor.getUser("Test_User", "UserTest");

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
        User nonUser = QueryExecutor.getUser("!something", "UserTest");
        assertNull("getUser() did not return null when given a username not in the db", nonUser);
    }
    
}
