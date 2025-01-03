import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbTestHelper {

    public static Connection prepareUsersTestTable() {
        try {
            Connection mySql = DBConnectorTest.connectToDB();
            PreparedStatement dropTransactionsTable = mySql.prepareStatement("drop table if exists transactions_test");
            PreparedStatement dropUsersTable = mySql.prepareStatement("drop table if exists users_test;");
            dropTransactionsTable.executeUpdate();
            dropUsersTable.executeUpdate();
            PreparedStatement createTable = mySql.prepareStatement("" +
                    "    CREATE TABLE IF NOT EXISTS users_test (" +
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

    //must call after prepareUsersTestTable to ensure referential integrity
    public static Connection prepareTransactionsTestTable() {

        try {
            Connection mySql = DBConnectorTest.connectToDB();
            PreparedStatement dropUserTable = mySql.prepareStatement("drop table if exists transactions_test;");
            dropUserTable.executeUpdate();
            PreparedStatement createTable = mySql.prepareStatement("" +
                    "    CREATE TABLE IF NOT EXISTS transactions_test (" +
                    "    id INT AUTO_INCREMENT PRIMARY KEY," +
                    "    description VARCHAR(255) default ''," +
                    "    amount DECIMAL(10,2) default 0.00," +
                    "    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "    user_id int," +
                    "    is_income tinyint default 0," +
                    "    category VARCHAR(50) DEFAULT 'Miscellaneous'" +
                    "    FOREIGN KEY (user_id) REFERENCES users_test(id) on delete cascade);");
            createTable.executeUpdate();
            return mySql;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void insertTestUser(Connection mySql, String username, String pw) {

        try {
            PreparedStatement addUser = mySql.prepareStatement("insert into users_test (username, password_hash) values( ?, ? );");
            addUser.setString(1, username);
            addUser.setString(2, pw);
            addUser.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertTestTransaction(Connection mySql){};//todo: add args & method body

}
