package han.jiayun.campsite.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.FromTo;
import han.jiayun.campsite.reservation.model.RequestedReservation;

/**
 * 
 * @author Jiayun Han
 *
 */
public interface ReservationService {
	
	// return reservation ID
	String createReservation(RequestedReservation request);
	
	void cancelReservation(String reservationId);
	
	ConfirmedReservation modifyReservation(String reservationId, RequestedReservation patch);
	
	ConfirmedReservation getReservation(String reservationId);

	void validatePostRequest(RequestedReservation request);

	List<FromTo> findAvailableDateRanges(Optional<LocalDate> optionalStart, Optional<LocalDate> optionalEnd);
}
