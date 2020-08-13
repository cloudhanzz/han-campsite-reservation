package han.jiayun.campsite.reservation.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * An exception of this class is triggered whenever the requested reservation cannot
 * be found.
 * 
 * @author Jiayun Han
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservationNotFoundException extends RestException {

	private static final long serialVersionUID = 1L;
	private static final String FORMAT_MESSAGE = "Reservation %s not found";
	private static final String NOTE = "Please provide the correct reservation ID";

	/**
	 * @param reservationId
	 *            The id of the reservation
	 */
	public ReservationNotFoundException(String reservationId) {
		super(String.format(FORMAT_MESSAGE, reservationId), HttpStatus.NOT_FOUND, NOTE);
	}
}
