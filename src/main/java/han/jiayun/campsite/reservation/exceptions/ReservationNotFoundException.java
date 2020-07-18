package han.jiayun.campsite.reservation.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * An exception of this class is triggered whenever the requested account cannot
 * be found.
 * 
 * @author Jiayun Han
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservationNotFoundException extends RestException {

	private static final String FORMAT_MESSAGE = "Reservation %s not found";

	private static final long serialVersionUID = 1L;

	/**
	 * @param requestId
	 *            The request ID string for logging purpose
	 */
	public ReservationNotFoundException(String reservationId, String note) {
		super(String.format(FORMAT_MESSAGE, reservationId), HttpStatus.NOT_FOUND, note);
	}
}
