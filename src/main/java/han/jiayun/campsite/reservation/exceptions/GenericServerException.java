package han.jiayun.campsite.reservation.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * An exception of this class is triggered if the server cannot patch the requested account 
 * 
 * @author Jiayun Han
 *
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GenericServerException extends RestException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @param message
	 */
	public GenericServerException(String message) {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR, "This is a generic internal server error");
	}
}
