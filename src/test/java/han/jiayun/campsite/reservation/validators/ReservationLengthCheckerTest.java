package han.jiayun.campsite.reservation.validators;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import han.jiayun.campsite.reservation.exceptions.InvalidReservationLengthException;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.RequestedReservation;

@DisplayName("Test Reservation Length Checker")
public class ReservationLengthCheckerTest {
	
	private static ReservationLengthChecker checker;
	
	@BeforeAll
	public static void setUpClass() {
		checker = new ReservationLengthChecker();
	}

	
	@DisplayName("Test departure date is before or equal to arrival date and expect exception thrown")
	@ParameterizedTest
	@ValueSource(ints = {0, 5, 10})
	public void departureNotAfterArrival(int departure_n_days_before_arrival) {
		
		RequestedReservation request = new RequestedReservation();
		
		LocalDate now = LocalDate.now();
		LocalDate arrival = now.plusDays(1);
		LocalDate departure = arrival.minusDays(departure_n_days_before_arrival);
		Schedule schedule = new Schedule(arrival, departure);	
		
		request.setDates(schedule);
		
		assertThrows(InvalidReservationLengthException.class, () -> checker.check(request.getUser(), request.getDates()));
	}
	

	@Test
	@DisplayName("Test reserved more days than allowed and expect exception thrown")
	public void maxDaysPassed() {
		
		RequestedReservation request = new RequestedReservation();
		
		LocalDate now = LocalDate.now();
		LocalDate arrival = now.plusDays(1);
		LocalDate departure = arrival.plusDays(4);
		Schedule schedule = new Schedule(arrival, departure);
		
        request.setDates(schedule);
		
        assertThrows(InvalidReservationLengthException.class, () -> checker.check(request.getUser(), request.getDates()));
	}
	

	
	@DisplayName("Test attempted dates are valid and expect no exception thrown: 😱")
	@Test
	public void validDatesProvided() {
		
		RequestedReservation request = new RequestedReservation();	
		
		LocalDate now = LocalDate.now();
		LocalDate arrival = now.plusDays(10);
		LocalDate departure = now.plusDays(12);		
		Schedule schedule = new Schedule(arrival, departure);	
		
		request.setDates(schedule);
		
		checker.check(request.getUser(), request.getDates());
	}	
}
