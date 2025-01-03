import com.expense_tracker.Transaction;
import com.expense_tracker.User;
import com.expense_tracker.db.TransactionQueryExecutor;
import com.expense_tracker.db.UserQueryExecutor;
import org.junit.Test;
import java.sql.Connection;
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
            //todo: add this method => DbTestHelper.insertTestTransaction(mySql, "something", "pw");
            Transaction nonTransaction = TransactionQueryExecutor.getTransaction(2, IS_TEST);
            assertNull("getTransaction() did not return null when given a transaction not in the db", nonTransaction);
        }

        @Test
        public void updateTransactionTest_Exists() {
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            //todo: add this method => DbTestHelper.insertTestTransaction(mySql, "beforeUpdate", "pw");
            Transaction originalTransaction = TransactionQueryExecutor.getTransaction(1, IS_TEST);
            //todo: add this method => originalTransaction.setAmount(42.00);
            Transaction transactionUpdated = TransactionQueryExecutor.updateTransaction(1, originalTransaction, IS_TEST);
            assertEquals("Query Executor did not update the transaction", transactionUpdated);
            Transaction updatedtransaction = TransactionQueryExecutor.getTransaction(1, IS_TEST);
            //todo: add this method => Double amnt = updatedtransaction.getTransactionAmount();
            //assertEquals("updateTransaction() did not properly update transaction amount", 42.0, amnt);
        }

        @Test
        public void updateTransactionTest_Not_Exists() {
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            //todo: add this method => DbTestHelper.insertTestTransaction(mySql, "beforeUpdate", "pw");
            Transaction notInDB = new Transaction(2, 777, 1);
            Transaction transactionUpdated = TransactionQueryExecutor.updateTransaction(1, notInDB, IS_TEST);
            assertNull("Query Executor did not return null to update transaction not in db", transactionUpdated);
        }

        @Test
        public void createTransactionTest() {
            DbTestHelper.prepareUsersTestTable();
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            DbTestHelper.insertTestUser(mySql, "test", "pw");
            User user = UserQueryExecutor.findUserByName("test", IS_TEST);
            Transaction newTransaction = TransactionQueryExecutor.createTransaction(123,
                    "test", "test", user.getId(), false, IS_TEST);
            assertEquals("first transaction created should be given id=1 by db", 1, newTransaction.getId());
        }

        @Test
        public void deleteTransactionTest() {
            Connection mySql = DbTestHelper.prepareTransactionsTestTable();
            DbTestHelper.insertTestTransaction(mySql, 404, "delete_transaction");
            User user = new User();
            Transaction toDelete = TransactionQueryExecutor.getTransaction(1, IS_TEST);
            TransactionQueryExecutor.deleteTransaction(toDelete.getId(), user, IS_TEST);
            Transaction nullTransaction = TransactionQueryExecutor.getTransaction(1, IS_TEST);
            assertNull("transaction that should have been deleted was found", nullTransaction);
        }
    }
