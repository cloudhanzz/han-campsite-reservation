package han.jiayun.campsite.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception of this class is if the campsite is reserved for less than 1 day or more than 3 days
 * 
 * @author Jiayun Han
 *
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidReservationLengthException extends RestException {

	private static final String NOTE = "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance";

	private static final long serialVersionUID = 1L;
	
	// TODO use static instance
	public InvalidReservationLengthException(String message) {
		super(message, HttpStatus.UNPROCESSABLE_ENTITY, NOTE);
	}
}
