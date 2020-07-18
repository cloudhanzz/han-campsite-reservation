package han.jiayun.campsite.reservation.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * An exception of this class is triggered upon receiving a request body missing
 * one or more required attributes. This is a more generic exception.
 * 
 * @author Jiayun Han
 *
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidRequestBodyException extends RestException {

	private static final long serialVersionUID = 1L;
	private static final String NOTE = "Please ensure provide the correct required information";

	/**
	 * 
	 * @param message
	 */
	public InvalidRequestBodyException(String message) {
		super("RequestBody contains no " + message, HttpStatus.NOT_ACCEPTABLE, NOTE);
	}
}
