import com.expense_tracker.services.AuthenticationService;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

public class AuthenticationServiceTest {

    @Test
    public void generateHash_Test() {
        String txt = "password1234";
        String encrypted = AuthenticationService.generateHash(txt);
        assertNotEquals("Authentication Server didn't return hashed String","password1234", encrypted);
    }
}
