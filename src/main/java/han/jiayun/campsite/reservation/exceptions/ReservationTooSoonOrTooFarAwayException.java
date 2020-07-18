package han.jiayun.campsite.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception of this class is if the campsite is reserved less than 1 day
 * ahead of arrival or more than 1 month (30 days) in advance.
 * 
 * @author Jiayun Han
 *
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ReservationTooSoonOrTooFarAwayException extends RestException {

	private static final String NOTE = "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance";

	private static final long serialVersionUID = 1L;
	
	// TODO use static instance
	public ReservationTooSoonOrTooFarAwayException(String message) {
		super(message, HttpStatus.UNPROCESSABLE_ENTITY, NOTE);
	}
}
