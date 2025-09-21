import com.expense_tracker.Transaction;
import com.expense_tracker.db.TableNameProvider;
import com.expense_tracker.db.TransactionQueryExecutor;
import com.expense_tracker.db.UserQueryExecutor;


import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionQueryExecutorTest {

    private Connection mySql;
    private TransactionQueryExecutor transactionQueryExecutor;
    private UserQueryExecutor userQueryExecutor;

    @Before
    public void setup() {
        try {

            this.mySql = DbTestHelper.prepareTestTables();
            //the DB used for unit-testing has no tableSuffix
            TableNameProvider provider = new TableNameProvider("");

            this.transactionQueryExecutor = new TransactionQueryExecutor(mySql, provider);
            this.userQueryExecutor = new UserQueryExecutor(mySql, provider);

        } catch (Exception e) {
            System.out.println("Error preparing test objects " + e.getLocalizedMessage());
        }
    }

        @Test
        public void getTransactionTest_Exists() {
            
            DbTestHelper.insertTestTransaction(mySql, 42, "testing");

            Transaction fetchedTransaction = transactionQueryExecutor.getTransaction(1);

            if (fetchedTransaction == null) {
                fail("TransactionQueryExecutor.getTransaction() returned Null instead of expected transaction");
            }

            String fetchedTransactionDescription = fetchedTransaction.getDescription();
            assertEquals("TransactionQueryExecutor.getTransaction() did not return expected transaction",
                    "testing", fetchedTransactionDescription);
        }

        @Test
        public void getTransactionTest_DoesNotExist() {
            
            DbTestHelper.insertTestTransaction(mySql, 1234, "anything");
            Transaction nonTransaction = transactionQueryExecutor.getTransaction(2);
            assertNull("getTransaction() did not return null when given a transaction not in the db", nonTransaction);
        }

        @Test
        public void createTransactionTest() {
            DbTestHelper.insertTestUser(mySql, "test", "pw");
            Transaction newTransaction = transactionQueryExecutor.createTransaction(123,
                    "test", "test", 1);
            assertEquals("first transaction created should be given id=1 by db", 1, newTransaction.getId());
        }

        @Test
        public void getAllTransactionsTest() {

            DbTestHelper.insertTestUser(mySql, "test user", "pw");
            //create 2nd user to ensure user 1 only gets transactions belonging to them
            userQueryExecutor.createUser("second_user", "pwh", "user");
            Transaction t1 = transactionQueryExecutor.createTransaction(1, "first", "", 1);
            Transaction t2 = transactionQueryExecutor.createTransaction(2, "second", "", 1);
            //t3 should not be returned in list of test user's transactions
            Transaction t3 = transactionQueryExecutor.createTransaction(99, "other_user", "", 2);

            List<Transaction> transactions = transactionQueryExecutor.getAllTransactions(1);
            int numTransactions = transactions.size();
            assertEquals("getAllTransactions did not return expected number of transactions", 2, numTransactions);
        }

        @Test
        public void updateTransactionTest_Exists() {
            
            DbTestHelper.insertTestTransaction(mySql, 1234, "to_update");
            Transaction originalTransaction = transactionQueryExecutor.getTransaction(1);
            originalTransaction.setAmount(42.00);
            boolean transactionUpdated = transactionQueryExecutor.updateTransaction(originalTransaction);
            assertTrue("Query Executor returned null when updating transaction", transactionUpdated);
            Transaction updatedtransaction = transactionQueryExecutor.getTransaction(1);
            double amount = updatedtransaction.getAmount();
            assertEquals("updateTransaction did not properly update transaction amount", 42.00, amount, 0.001);
        }

        @Test
        public void updateTransactionTest_Not_Exists() {
            
            DbTestHelper.insertTestTransaction(mySql, 775, "just giving the db an entry");
            Transaction notInDB = new Transaction(2, 777, 1);
            boolean transactionUpdated = transactionQueryExecutor.updateTransaction( notInDB);
            assertFalse("updateTransaction returned true for transaction not in db", transactionUpdated);
        }

    @Test
    public void deleteTransactionTest_Exists() {
        DbTestHelper.insertTestTransaction(mySql, 404, "delete_transaction");
        transactionQueryExecutor.deleteTransaction(1);
        Transaction nullTransaction = transactionQueryExecutor.getTransaction(1);
        assertNull("transaction that should have been deleted was found", nullTransaction);
    }

    @Test
    public void deleteTransactionTest_Not_Exists() {
        Transaction notInDb = new Transaction(1, 123,1);
        boolean deleted = transactionQueryExecutor.deleteTransaction(1);
        assertFalse("deleteTransaction should return false if transaction not in db", deleted);
    }

    @Test
    public void getTransactionsByAmount_Test() {
        
        DbTestHelper.insertTestUser(mySql, "test user", "pw");;

        //make 2 out of 4 transactions in the query range
        Transaction t1 = transactionQueryExecutor.createTransaction(1, "first", "n", 1);
        Transaction t2 = transactionQueryExecutor.createTransaction(55, "second", "y", 1);
        Transaction t3 = transactionQueryExecutor.createTransaction(99, "third", "n", 1);
        Transaction t4 = transactionQueryExecutor.createTransaction(98.9, "fourth", "y", 1);

        List<Transaction> transactions = transactionQueryExecutor.getTransactionsByAmount(1, 2, 98.9);
        int numTransactions = transactions.size();
        assertEquals("getTransactionsByAmount did not return expected number of transactions", 2, numTransactions);
        assertEquals("getTransactionsByAmount did not return the correct transactions",
                transactions.get(1).getCategory(), "y");
    }

    @Test
    public void getTransactionsByCategory_Test() {

        DbTestHelper.insertTestUser(mySql, "test user", "pw");

        Transaction t1 = transactionQueryExecutor.createTransaction(1, "first", "y", 1);
        Transaction t2 = transactionQueryExecutor.createTransaction(2, "second", "y", 1);
        Transaction t3 = transactionQueryExecutor.createTransaction(3, "third", "n", 1);
        Transaction t4 = transactionQueryExecutor.createTransaction(1, "fourth", "y", 1);
        
        List<Transaction> transactions = transactionQueryExecutor.getTransactionsByCategory(1, "y");
        int numTransactions = transactions.size();
        assertEquals("getTransactionsByAmount did not return expected number of transactions", 3, numTransactions);
    }

    @Test
    public void getTransactionsByDate_Test() {

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
            Transaction t = transactionQueryExecutor.createTransaction(1, "_", "_", 1,
                    d);
        }
        List<Transaction> transactions = transactionQueryExecutor.getTransactionsByDate(1, rangeStart, rangeEnd);
        int numTransactions = transactions.size();
        assertEquals("getTransactionsByAmount did not return expected number of transactions", 3, numTransactions);
    }

}
