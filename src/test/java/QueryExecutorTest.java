import com.expense_tracker.User;
import com.expense_tracker.db.QueryExecutor;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class QueryExecutorTest {


    @Test
    public void getUserTest_Exists() {

        try {
            Connection mySql = DbTestHelper.prepareTestDb();
            PreparedStatement addUser = mySql.prepareStatement("insert into UserTest (username, password_hash) values( ?, ? );");
            addUser.setString(1, "Test_User");
            addUser.setString(2, "pw");
            addUser.executeUpdate();
            User fetchedUser = QueryExecutor.getUser("Test_User", "UserTest");
            if (fetchedUser == null) {
                fail("QueryExecutor.getUser() returned Null instead of expected User");
            }
            String fetchedUsername = fetchedUser.getUsername();
            assertEquals("QueryExecutor.getUser() did not return expected User", "Test_User", fetchedUser.getUsername());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            fail("An unexpected exception occurred when querying database");
        }

    }
}
