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
@ResponseStatus(HttpStatus.CONFLICT)
public class DatesUnavailableException extends RestException {

	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "The attempted dates are not available.";
	private static final String NOTE = "The same or overlapping date(s) cannot be made by multiple reservations";
	
	private static DatesUnavailableException INSTANCE = new DatesUnavailableException(MESSAGE);
	
	private DatesUnavailableException(String message) {
		super(message, HttpStatus.CONFLICT, NOTE);
	}
	
	public static DatesUnavailableException instance() {
		return INSTANCE;
	}
}
