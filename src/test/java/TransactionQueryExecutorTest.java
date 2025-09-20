import com.expense_tracker.Transaction;
import com.expense_tracker.User;
import com.expense_tracker.db.TransactionQueryExecutor;
import com.expense_tracker.db.UserQueryExecutor;
import org.junit.Test;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionQueryExecutorTest {


        @Test
        public void getTransactionTest_Exists() {
            Connection mySql = DbTestHelper.prepareTestTables();
            DbTestHelper.insertTestTransaction(mySql, 42, "testing");

            Transaction fetchedTransaction = TransactionQueryExecutor.getTransaction(1);

            if (fetchedTransaction == null) {
                fail("TransactionQueryExecutor.getTransaction() returned Null instead of expected transaction");
            }

            String fetchedTransactionDescription = fetchedTransaction.getDescription();
            assertEquals("TransactionQueryExecutor.getTransaction() did not return expected transaction",
                    "testing", fetchedTransactionDescription);
        }

        @Test
        public void getTransactionTest_DoesNotExist() {
            Connection mySql = DbTestHelper.prepareTestTables();
            DbTestHelper.insertTestTransaction(mySql, 1234, "anything");
            Transaction nonTransaction = TransactionQueryExecutor.getTransaction(2);
            assertNull("getTransaction() did not return null when given a transaction not in the db", nonTransaction);
        }

        @Test
        public void createTransactionTest() {
            DbTestHelper.prepareTestTables();
            Connection mySql = DbTestHelper.prepareTestTables();
            DbTestHelper.insertTestUser(mySql, "test", "pw");
            Transaction newTransaction = TransactionQueryExecutor.createTransaction(123,
                    "test", "test", 1);
            assertEquals("first transaction created should be given id=1 by db", 1, newTransaction.getId());
        }

        @Test
        public void getAllTransactionsTest() {
            DbTestHelper.prepareTestTables();
            Connection mySql = DbTestHelper.prepareTestTables();
            DbTestHelper.insertTestUser(mySql, "test user", "pw");
            //create 2nd user to ensure user 1 only gets transactions belonging to them
            UserQueryExecutor.createUser("second_user", "pwh", "user");
            Transaction t1 = TransactionQueryExecutor.createTransaction(1, "first", "", 1);
            Transaction t2 = TransactionQueryExecutor.createTransaction(2, "second", "", 1);
            Transaction t3 = TransactionQueryExecutor.createTransaction(99, "other_user", "", 2);

            User ourUser = UserQueryExecutor.getUser(1);
            List<Transaction> transactions = TransactionQueryExecutor.getAllTransactions(1);
            int numTransactions = transactions.size();
            assertEquals("getAllTransactions did not return expected number of transactions", 2, numTransactions);
        }

        @Test
        public void updateTransactionTest_Exists() {
            Connection mySql = DbTestHelper.prepareTestTables();
            DbTestHelper.insertTestTransaction(mySql, 1234, "to_update");
            Transaction originalTransaction = TransactionQueryExecutor.getTransaction(1);
            originalTransaction.setAmount(42.00);
            boolean transactionUpdated = TransactionQueryExecutor.updateTransaction(originalTransaction);
            assertTrue("Query Executor returned null when updating transaction", transactionUpdated);
            Transaction updatedtransaction = TransactionQueryExecutor.getTransaction(1);
            double amount = updatedtransaction.getAmount();
            assertEquals("updateTransaction did not properly update transaction amount", 42.00, amount, 0.001);
        }

        @Test
        public void updateTransactionTest_Not_Exists() {
            Connection mySql = DbTestHelper.prepareTestTables();
            DbTestHelper.insertTestTransaction(mySql, 775, "just giving the db an entry");
            Transaction notInDB = new Transaction(2, 777, 1);
            boolean transactionUpdated = TransactionQueryExecutor.updateTransaction( notInDB);
            assertFalse("updateTransaction returned true for transaction not in db", transactionUpdated);
        }

        @Test
        public void deleteTransactionTest_Exists() {
            Connection mySql = DbTestHelper.prepareTestTables();
            DbTestHelper.insertTestTransaction(mySql, 404, "delete_transaction");
            TransactionQueryExecutor.deleteTransaction(1);
            Transaction nullTransaction = TransactionQueryExecutor.getTransaction(1);
            assertNull("transaction that should have been deleted was found", nullTransaction);
        }

    @Test
    public void deleteTransactionTest_Not_Exists() {
        Connection mySql = DbTestHelper.prepareTestTables();
        Transaction notInDb = new Transaction(1, 123,1);
        boolean deleted = TransactionQueryExecutor.deleteTransaction(1);
        assertFalse("deleteTransaction should return false if transaction not in db", deleted);
    }

    @Test
    public void getTransactionsByAmount_Test() {
        DbTestHelper.prepareTestTables();
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "test user", "pw");;


        //make 2 out of 4 transactions in the query range
        Transaction t1 = TransactionQueryExecutor.createTransaction(1, "first", "n", 1);
        Transaction t2 = TransactionQueryExecutor.createTransaction(55, "second", "y", 1);
        Transaction t3 = TransactionQueryExecutor.createTransaction(99, "third", "n", 1);
        Transaction t4 = TransactionQueryExecutor.createTransaction(98.9, "fourth", "y", 1);

        List<Transaction> transactions = TransactionQueryExecutor.getTransactionsByAmount(1, 2, 98.9);
        int numTransactions = transactions.size();
        assertEquals("getTransactionsByAmount did not return expected number of transactions", 2, numTransactions);
        assertEquals("getTransactionsByAmount did not return the correct transactions",
                transactions.get(1).getCategory(), "y");
    }

    @Test
    public void getTransactionsByCategory_Test() {
        DbTestHelper.prepareTestTables();
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "test user", "pw");

        Transaction t1 = TransactionQueryExecutor.createTransaction(1, "first", "y", 1);
        Transaction t2 = TransactionQueryExecutor.createTransaction(2, "second", "y", 1);
        Transaction t3 = TransactionQueryExecutor.createTransaction(3, "third", "n", 1);
        Transaction t4 = TransactionQueryExecutor.createTransaction(1, "fourth", "y", 1);
        
        List<Transaction> transactions = TransactionQueryExecutor.getTransactionsByCategory(1, "y");
        int numTransactions = transactions.size();
        assertEquals("getTransactionsByAmount did not return expected number of transactions", 3, numTransactions);
    }

    @Test
    public void getTransactionsByDate_Test() {
        DbTestHelper.prepareTestTables();
        Connection mySql = DbTestHelper.prepareTestTables();
        DbTestHelper.insertTestUser(mySql, "test user", "pw");

        //create 5 dates, 3 of which should be returned by the query, one too old, one too new
        LocalDate rangeStart = LocalDate.of(2023, 1, 1);
        LocalDate rangeEnd = LocalDate.of(2024, 1, 1);
        LocalDate inRange = LocalDate.of(2023, 6, 1);
        LocalDate tooOld = LocalDate.of(2022, 12, 25);
        LocalDate tooNew = LocalDate.of(2024, 1, 2);


        LocalDate[] dates = {rangeStart, rangeEnd, inRange, tooOld, tooNew};
        for (int i = 0; i < 5; i++) {
            //for each date, create a transaction using that date
            LocalDate d = dates[i];
            Transaction t = TransactionQueryExecutor.createTransaction(1, "_", "_", 1,
                    d);
        }
        List<Transaction> transactions = TransactionQueryExecutor.getTransactionsByDate(1, rangeStart, rangeEnd);
        int numTransactions = transactions.size();
        assertEquals("getTransactionsByAmount did not return expected number of transactions", 3, numTransactions);
    }

}
