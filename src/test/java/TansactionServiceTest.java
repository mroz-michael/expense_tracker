import com.expense_tracker.Transaction;
import com.expense_tracker.db.TransactionQueryExecutor;
import com.expense_tracker.services.TransactionService;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.*;

public class TansactionServiceTest {
    private static final boolean IS_TEST = true;


    //call before each test, resets users_test and transactions_test tables and creates a user
    private static Connection prepareDB() {
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "test_user", "pw");
        return mySql;
    }

    @Test
    public void createTransaction_Test_Valid() {
        prepareDB();
        TransactionService.createTransaction(42.02, "test", "test", 1);
        Transaction t = TransactionQueryExecutor.getTransaction(1);
        assertEquals("created transaction did not get added to the database", t.getAmount(), 42.02, .001);
    }

    @Test
    public void getTransaction_Test_Valid() {
        prepareDB();
        TransactionService.createTransaction(42, "test", "test", 1);
        TransactionService.createTransaction(1234, "test2", "test", 1);
        Transaction t = TransactionService.getTransaction(2);
        assertEquals("getTransaction() did not return correct resource", 2, t.getId());
    }

    @Test
    public void getTransaction_Test_Invalid() {
        prepareDB();
        TransactionService.createTransaction(42, "test", "test", 1);
        Transaction t = TransactionService.getTransaction(2);
        assertNull("getTransaction() should return null if given an invalid id", t);
    }

    @Test
    public void getAllTransactions_Test() {
        Connection mySql = prepareDB();
        DbTestHelper.insertTestUser(mySql, "second_user", "anything");
        //give 5 transactions to user 1, then 3 to user 2, then 1 to user 1.
        for (int i = 0; i < 5; i++) {
            TransactionService.createTransaction(i +1, "for user1","t", 1);
        }
        for (int i = 0; i < 3; i++) {
            TransactionService.createTransaction(i +1, "for user2","t", 2);
        }
        TransactionService.createTransaction(42, "last one for user1", "_", 1);
        List<Transaction> transactionList = TransactionService.getAllTransactions(1);
        for (int i = 0; i < transactionList.size(); i++) {
            int userId = transactionList.get(i).getUserId();
            assertEquals("transactions list contained transaction not belonging to user", 1, userId);
        }
        assertEquals("transaction list did not contain correct amount of transactions", 6, transactionList.size());
    }

    @Test
    public void updateTransaction_Test() {
        prepareDB();
        TransactionService.createTransaction(42, "update me", "t", 1);
        Transaction toUpdate = TransactionService.getTransaction(1);
        toUpdate.setAmount(54);
        TransactionService.updateTransaction(toUpdate);
        Transaction updated = TransactionService.getTransaction(1);
        assertEquals("transaction update was not stored properly in db", 54, updated.getAmount(), 0.01);
    }

    @Test
    public void deleteTransaction_Test_Valid() {
        prepareDB();
        TransactionService.createTransaction(1234, "-", "_", 1);
        TransactionService.createTransaction(4321, "delete me", "_", 1);
        boolean deleted = TransactionService.deleteTransaction(2);
        assertTrue("deleteTransaction returned false", deleted);
        List<Transaction> tList = TransactionService.getAllTransactions(1);
        int size = tList.size();
        assertEquals("deleteTransaction did not remove the transaction from the db", 1, size);
    }

    @Test
    public void deleteTransaction_Test_InValid() {
        prepareDB();
        TransactionService.createTransaction(1234, "-", "_", 1);
        boolean deleted = TransactionService.deleteTransaction(2);
        assertFalse("deleteTransaction returned true when given an invalid transaction", deleted);
    }

}
