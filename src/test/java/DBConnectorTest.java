import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.*;
import org.junit.Test;

import com.expense_tracker.db.DBConnector;

    public class DBConnectorTest {

        private static final String PATH_TO_CONFIG = "..\\expense_tracker\\src\\main\\java\\com\\expense_tracker\\resources\\config.properties";

        @Test
        public void testConnection() {

            try {
                Connection mySql = DBConnector.connect(PATH_TO_CONFIG);
                Statement stmt = mySql.createStatement();
                PreparedStatement createTable = mySql.prepareStatement("create table if not exists Testing ( testcol varchar(10))");
                createTable.executeUpdate();
                PreparedStatement insertRow = mySql.prepareStatement("insert into Testing (testcol) VALUES('test')");
                insertRow.executeUpdate();
                ResultSet res = stmt.executeQuery("select * from Testing");
                PreparedStatement rmvTable = mySql.prepareStatement("drop table Testing");
                rmvTable.executeUpdate();
                res.next();
                String colValue = res.getString(1);
                assertEquals("DB connection was unable to create new table and insert into it", "test", colValue);


            } catch (SQLException | IOException e) {
                fail("Unexpected exception: " + e.getMessage());
            }

        }
    }

