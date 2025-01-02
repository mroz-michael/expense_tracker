import com.expense_tracker.User;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class UserTest {

    @Test
    public void testDeleteUser_As_Non_Admin() {
        User regUser = new User(1, "regular user", false);
        User toDelete = new User(2, "I wont be deleted by a non-admin", false);
        assertNull("attempting to delete a user as a non-admin should return null", regUser.deleteUser(toDelete));
    }

    /*todo: add test for successful user deletion once implementing
        @Test
        public void testDeleteUser_As_Admin() {
        }

        add another test for admin being unable to delete other admin
     */

}
