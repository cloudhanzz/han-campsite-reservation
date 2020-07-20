package han.jiayun.campsite.reservation.validators;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import han.jiayun.campsite.reservation.exceptions.InvalidRequestBodyException;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.UserInfo;

/**
 * 
 * @author Jiayun Han
 *
 */
@DisplayName("Test Pre Checker")
public class UserInfoCheckerTest {

	private static UserInfoChecker checker;

	@BeforeAll
	public static void setUpClass() {
		checker = new UserInfoChecker();
	}

	@DisplayName("Test user information is valid: 😱")
	@Test
	public void allProvided() {
		

		UserInfo user = new UserInfo("First", "Last", "xyz.abc@xxxx.com");
		RequestedReservation request = new RequestedReservation();
		request.setUser(user);

		checker.check(request.getUser(), request.getDates());
	}

	@DisplayName("Test first name is not valid")
	@Test
	public void testInvalidFirstName() {

		UserInfo user = new UserInfo("33First", "Last", "xyz.abc@xxxx.com");
		RequestedReservation request = new RequestedReservation();
		request.setUser(user);
		Exception ex = assertThrows(InvalidRequestBodyException.class, () -> checker.check(request.getUser(), request.getDates()));
		assertTrue(ex.getMessage().contains("First Name"));
	}

	@DisplayName("Test last name is not valid")
	@Test
	public void testInvalidLastName() {

		UserInfo user = new UserInfo("First", "-", "xyz.abc@xxxx.com");
		RequestedReservation request = new RequestedReservation();
		request.setUser(user);
		Exception ex = assertThrows(InvalidRequestBodyException.class, () -> checker.check(request.getUser(), request.getDates()));
		assertTrue(ex.getMessage().contains("Last Name"));
	}
	
	@DisplayName("Test email is not valid")
	@ParameterizedTest
	@ValueSource(strings = {"abc.de@google", "123@123com", "123@@gmail.com"})
	public void testInvalidEmails(String email) {

		UserInfo user = new UserInfo("Jone", "Done", email);
		RequestedReservation request = new RequestedReservation();
		request.setUser(user);
		Exception ex = assertThrows(InvalidRequestBodyException.class, () -> checker.check(request.getUser(), request.getDates()));
		assertTrue(ex.getMessage().contains("no valid Email"));
	}
	
	@DisplayName("Test email is valid")
	@ParameterizedTest
	@ValueSource(strings = {"john.doe@gmail.com", "123@gmail.com", "123@123.info"})
	public void testGoodEmails(String email) {

		UserInfo user = new UserInfo("Jone", "Done", email);
		RequestedReservation request = new RequestedReservation();
		request.setUser(user);
		checker.check(request.getUser(), request.getDates());
	}
}
