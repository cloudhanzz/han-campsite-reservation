package han.jiayun.campsite.reservation.validators;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import han.jiayun.campsite.reservation.exceptions.InvalidReservationLengthException;
import han.jiayun.campsite.reservation.model.RequestedReservation;

/**
 * Checking the length of the reservation.
 * <p>
 * The campsite can be reserved for minimum of 1 day and maximum of 3 days.
 * 
 * @author Jiayun Han
 *
 */
public final class ReservationLengthChecker implements RequestValidator {

	private static final int MAX_RESERVATION_DAYS = 3;
	private static final int MIN_RESERVATION_DAYS = 1;

	public static final String ERROR_DEPARTURE_NOT_AFTER_ARRIVAL = "You are supposed to reserve minimal "
			+ MIN_RESERVATION_DAYS + " day(s)";
	public static final String ERROR_BEYOND_MAX_RESERVATION_DAYS = "Currently no more than " + MAX_RESERVATION_DAYS
			+ " days can be reserved";

	@Override
	public void check(RequestedReservation request) {		

		LocalDate arrival = request.getDates().getArrival();
		LocalDate departure = request.getDates().getDeparture();

		long days = ChronoUnit.DAYS.between(arrival, departure);

		String message = null;
		if (days < MIN_RESERVATION_DAYS) {
			message = ERROR_DEPARTURE_NOT_AFTER_ARRIVAL;
		} else if (days > MAX_RESERVATION_DAYS) {
			message = ERROR_BEYOND_MAX_RESERVATION_DAYS;
		}

		if (message != null) {
			throw new InvalidReservationLengthException(message);
		}
	}
}
