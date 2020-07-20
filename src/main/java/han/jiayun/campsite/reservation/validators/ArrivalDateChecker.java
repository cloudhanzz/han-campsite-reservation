package han.jiayun.campsite.reservation.validators;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import han.jiayun.campsite.reservation.exceptions.ReservationTooSoonOrTooFarAwayException;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;
import han.jiayun.campsite.reservation.service.RequestValidator;

/**s
 * Checking whether the reservation to be made will start too soon or too far
 * away.
 * <p>
 * The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1
 * month in advance.
 * <p>
 * In this application, for the sake of simplicity, 1 month is treated as 30
 * days.
 * 
 * @author Jiayun Hans
 *
 */
public final class ArrivalDateChecker implements RequestValidator {

	private static final int MIN_DAYS_IN_ADVANCE = 1;
	private static final int MAX_DAYS_IN_ADVANCE = 30;

	public static final String ERROR_LESS_THAN_MIN_DAYS_IN_ADVANCE = "The reservation should be made at least "
			+ MIN_DAYS_IN_ADVANCE + " day(s) in advance";
	public static final String ERROR_MORE_THAN_MAX_DAYS_IN_ADVANCE = "The reservation cannot be made more than "
			+ MAX_DAYS_IN_ADVANCE + " days in advance";

	@Override
	public void check(UserInfo user, Schedule dates) {
		
		LocalDate arrival = dates.getArrival();
		
		LocalDate now = LocalDate.now();
		long days = ChronoUnit.DAYS.between(now, arrival);

		String message = null;
		if (days < MIN_DAYS_IN_ADVANCE) {
			message = ERROR_LESS_THAN_MIN_DAYS_IN_ADVANCE;
		} else if (days > MAX_DAYS_IN_ADVANCE) {
			message = ERROR_MORE_THAN_MAX_DAYS_IN_ADVANCE;
		}

		if (message != null) {
			throw new ReservationTooSoonOrTooFarAwayException(message);
		}
		
	}
}
