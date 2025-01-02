import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbTestHelper {

    public static Connection prepareTestDb() {
        try {
            Connection mySql = DBConnectorTest.connectToDB();
            PreparedStatement dropTable = mySql.prepareStatement("drop table if exists UserTest;");
            dropTable.executeUpdate();
            PreparedStatement createTable = mySql.prepareStatement("" +
                    "    CREATE TABLE IF NOT EXISTS UserTest (" +
                    "    id INT AUTO_INCREMENT PRIMARY KEY," +
                    "    username VARCHAR(255)," +
                    "    password_hash VARCHAR(255)," +
                    "    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "    role VARCHAR(50) DEFAULT 'user'" +
                    ");");
            createTable.executeUpdate();
            return mySql;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
