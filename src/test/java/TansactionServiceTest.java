import com.expense_tracker.Transaction;
import com.expense_tracker.db.TransactionQueryExecutor;
import com.expense_tracker.services.TransactionService;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TansactionServiceTest {
    private static final boolean IS_TEST = true;


    //call before each test, resets users_test and transactions_test tables and creates a user
    private static void prepareDB() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "test_user", "pw");
    }

    @Test
    public void createTransaction_Test_Valid() {
        prepareDB();
        TransactionService.createTransaction(42.02, "test", "test", 1, IS_TEST);
        Transaction t = TransactionQueryExecutor.getTransaction(1, IS_TEST);
        assertEquals("created transaction did not get added to the database", t.getAmount(), 42.02, .001);
    }

    @Test
    public void getTransaction_Test_Valid() {
        prepareDB();
        TransactionService.createTransaction(42, "test", "test", 1, IS_TEST);
        TransactionService.createTransaction(1234, "test2", "test", 1, IS_TEST);
        Transaction t = TransactionService.getTransaction(2, IS_TEST);
        assertEquals("getTransaction() did not return correct resource", 2, t.getId());
    }

    @Test
    public void getTransaction_Test_Invalid() {
        prepareDB();
        TransactionService.createTransaction(42, "test", "test", 1, IS_TEST);
        Transaction t = TransactionService.getTransaction(2, IS_TEST);
        assertNull("getTransaction() should return null if given an invalid id", t);
    }

    @Test
    public void getTransactions_Test() {

    }

    @Test
    public void updateTransaction_Test() {

    }

    @Test
    public void deleteTransaction_Test() {

    }

}
