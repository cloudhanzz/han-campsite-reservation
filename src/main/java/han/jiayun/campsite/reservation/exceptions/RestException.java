package han.jiayun.campsite.reservation.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * This is the ancestor of all REST exception classes defined for this project,
 * responsible for centralizing logging the error message descriptively.
 * 
 * @author Jiayun Han
 *
 */
@Getter
public class RestException extends RuntimeException {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestException.class);

	private static final long serialVersionUID = 1L;
	
	private final HttpStatus httpStatus;
	private final String note;

	/**
	 * 
	 * @param message
	 *            The message provided by inherited class
	 */
	public RestException(String message, HttpStatus httpStatus, String note) {
		super(message);
		this.httpStatus = httpStatus;
		this.note = note;
		LOGGER.error(message);
	}
}
