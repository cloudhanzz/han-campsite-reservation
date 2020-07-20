package han.jiayun.campsite.reservation.validators;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import han.jiayun.campsite.reservation.exceptions.MissRequiredValuesException;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.UserInfo;

/**
 * 
 * @author Jiayun Han
 *
 */
@DisplayName("Test Pre Checker")
public class MissingValueCheckerTest {

	private static MissingValueChecker checker;
	private static UserInfo user;

	@BeforeAll
	public static void setUpClass() {
		checker = new MissingValueChecker();
		user = new UserInfo("First", "Last", "xyz.abc@xxxx.com");
	}

	@DisplayName("Test all information is provided: 😱")
	@Test
	public void allProvided() {

		LocalDate now = LocalDate.now();
		LocalDate arrival = now.plusDays(10);
		LocalDate departure = now.plusDays(12);

		RequestedReservation request = new RequestedReservation();
		request.setDates(new Schedule(arrival, departure));
		request.setUser(user);

		checker.check(request.getUser(), request.getDates());
	}

	@DisplayName("Test user information is not provided")
	@Test
	public void missingUser() {

		LocalDate now = LocalDate.now();
		LocalDate arrival = now.plusDays(10);
		LocalDate departure = now.plusDays(12);

		RequestedReservation request = new RequestedReservation();
		request.setDates(new Schedule(arrival, departure));

		assertThrows(MissRequiredValuesException.class, () -> checker.check(request.getUser(), request.getDates()));
	}

	@DisplayName("Test dates information is not provided")
	@Test
	public void missingDates() {

		RequestedReservation request = new RequestedReservation();
		request.setUser(user);

		assertThrows(MissRequiredValuesException.class, () -> checker.check(request.getUser(), request.getDates()));
	}
}
