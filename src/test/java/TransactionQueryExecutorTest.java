import com.expense_tracker.Transaction;
import com.expense_tracker.User;
import com.expense_tracker.db.TransactionQueryExecutor;
import com.expense_tracker.db.UserQueryExecutor;
import org.junit.Test;
import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionQueryExecutorTest {

        private final static boolean IS_TEST = true;

        @Test
        public void getTransactionTest_Exists() {
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            DbTestHelper.insertTestTransaction(mySql, 42, "testing");

            Transaction fetchedTransaction = TransactionQueryExecutor.getTransaction(1, IS_TEST);

            if (fetchedTransaction == null) {
                fail("TransactionQueryExecutor.getTransaction() returned Null instead of expected transaction");
            }

            String fetchedTransactionDescription = fetchedTransaction.getDescription();
            assertEquals("TransactionQueryExecutor.getTransaction() did not return expected transaction",
                    "testing", fetchedTransactionDescription);
        }

        @Test
        public void getTransactionTest_DoesNotExist() {
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            DbTestHelper.insertTestTransaction(mySql, 1234, "anything");
            Transaction nonTransaction = TransactionQueryExecutor.getTransaction(2, IS_TEST);
            assertNull("getTransaction() did not return null when given a transaction not in the db", nonTransaction);
        }

        @Test
        public void createTransactionTest() {
            DbTestHelper.prepareUsersTestTable();
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            DbTestHelper.insertTestUser(mySql, "test", "pw");
            Transaction newTransaction = TransactionQueryExecutor.createTransaction(123,
                    "test", "test", 1, false, IS_TEST);
            assertEquals("first transaction created should be given id=1 by db", 1, newTransaction.getId());
        }

        @Test
        public void getAllTransactionsTest() {
            DbTestHelper.prepareUsersTestTable();
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            DbTestHelper.insertTestUser(mySql, "test user", "pw");
            //create 2nd user to ensure user 1 only gets transactions belonging to them
            UserQueryExecutor.createUser("second_user", "pwh", "user", IS_TEST);

            Transaction t1 = TransactionQueryExecutor.createTransaction(1, "first", "", 1,
                    false, IS_TEST);
            Transaction t2 = TransactionQueryExecutor.createTransaction(2, "second", "", 1,
            false, IS_TEST);
            Transaction t3 = TransactionQueryExecutor.createTransaction(99, "other_user", "", 2,
                    false, IS_TEST);

            User ourUser = UserQueryExecutor.getUser(1, IS_TEST);
            List<Transaction> transactions = TransactionQueryExecutor.getAllTransactions(ourUser, IS_TEST);
            int numTransactions = transactions.size();
            assertEquals("getAllTransactions did not return expected number of transactions", 2, numTransactions);
        }


        @Test
        public void updateTransactionTest_Exists() {
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            DbTestHelper.insertTestTransaction(mySql, 1234, "to_update");
            Transaction originalTransaction = TransactionQueryExecutor.getTransaction(1, IS_TEST);
            originalTransaction.setAmount(42.00);
            boolean transactionUpdated = TransactionQueryExecutor.updateTransaction(originalTransaction, IS_TEST);
            assertTrue("Query Executor returned null when updating transaction", transactionUpdated);
            Transaction updatedtransaction = TransactionQueryExecutor.getTransaction(1, IS_TEST);
            double amount = updatedtransaction.getAmount();
            assertEquals("updateTransaction did not properly update transaction amount", 42.00, amount, 0.001);
        }

        @Test
        public void updateTransactionTest_Not_Exists() {
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            DbTestHelper.insertTestTransaction(mySql, 775, "just giving the db an entry");
            Transaction notInDB = new Transaction(2, 777, 1);
            boolean transactionUpdated = TransactionQueryExecutor.updateTransaction( notInDB, IS_TEST);
            assertFalse("updateTransaction returned true for transaction not in db", transactionUpdated);
        }

        @Test
        public void deleteTransactionTest_Exists() {
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            DbTestHelper.insertTestTransaction(mySql, 404, "delete_transaction");
            Transaction toDelete = TransactionQueryExecutor.getTransaction(1, IS_TEST);
            TransactionQueryExecutor.deleteTransaction(toDelete, IS_TEST);
            Transaction nullTransaction = TransactionQueryExecutor.getTransaction(1, IS_TEST);
            assertNull("transaction that should have been deleted was found", nullTransaction);
        }

    @Test
    public void deleteTransactionTest_Not_Exists() {
        Connection mySql = DbTestHelper.prepareTransactionsTestTable();
        Transaction notInDb = new Transaction(1, 123,1);
        boolean deleted = TransactionQueryExecutor.deleteTransaction(notInDb, IS_TEST);
        assertFalse("deleteTransaction should return false if transaction not in db", deleted);
    }

    }
