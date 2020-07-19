package han.jiayun.campsite.reservation.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import han.jiayun.campsite.reservation.exceptions.ReservationTooSoonOrTooFarAwayException;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.RequestedReservation;

@DisplayName("Test Arrival Date Checker")
public class ArrivalDateCheckerTest {
	
	private static ArrivalDateChecker checker;
	
	@BeforeAll
	public static void setUpClass() {
		checker = new ArrivalDateChecker();
	}

	
	@DisplayName("Test reservation should be made at least one day in advance")
	@ParameterizedTest
	@ValueSource(ints = {0, -5, -10})
	public void reservation_should_be_at_least_1_day_in_advance(int n_days_before_now) {
		
		RequestedReservation request = makeRequest(n_days_before_now);		
		Exception exception = assertThrows(ReservationTooSoonOrTooFarAwayException.class,() -> checker.check(request));
		assertEquals(ArrivalDateChecker.ERROR_LESS_THAN_MIN_DAYS_IN_ADVANCE, exception.getMessage());
	}
	
	@DisplayName("Test reservation can only be made up to 1 month in advance")
	@ParameterizedTest
	@ValueSource(ints = {31, 45, 60})
	public void reservation_cannot_be_more_than_1_month_in_advance(int n_days_after_now) {
		
		RequestedReservation request = makeRequest(n_days_after_now);		
		Exception exception = assertThrows(ReservationTooSoonOrTooFarAwayException.class,() -> checker.check(request));
		assertEquals(ArrivalDateChecker.ERROR_MORE_THAN_MAX_DAYS_IN_ADVANCE, exception.getMessage());
	}
	

	@DisplayName("Test reservation made in correct time in advance: 😱")
	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3})
	public void validArrivalProvided(int n_days_in_advance) {
		
		RequestedReservation request = makeRequest(n_days_in_advance);		
		checker.check(request);
	}


	private RequestedReservation makeRequest(int n_days_in_advance) {
		
		RequestedReservation request = new RequestedReservation();
		
		LocalDate now = LocalDate.now();
		LocalDate arrival = now.plusDays(n_days_in_advance);
		
		Schedule schedule = new Schedule(arrival, null);
		request.setDates(schedule);
		return request;
	}	
}
