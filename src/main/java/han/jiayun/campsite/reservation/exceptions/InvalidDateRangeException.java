package han.jiayun.campsite.reservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception of this class is if the attempted dates are not available
 * 
 * <P>The same or overlapping date(s) cannot be made by multiple reservations 
 * 
 * @author Jiayun Han
 *
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidDateRangeException extends RestException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "The starting date should not be after the ending date.";
	private static final String NOTE = "Please correct the staring and end dates";
	
	private static InvalidDateRangeException INSTANCE = new InvalidDateRangeException(MESSAGE);
	
	private InvalidDateRangeException(String message) {
		super(message, HttpStatus.NOT_ACCEPTABLE, NOTE);
	}
	
	public static InvalidDateRangeException instance() {
		return INSTANCE;
	}
}
