package han.jiayun.campsite.reservation.service;

import java.time.LocalDate;
import java.util.List;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.RequestedReservation;

//TODO add context aware
public interface ReservationService {
	
	// return reservation ID
	String createReservation(RequestedReservation request);
	
	boolean cancelReservation(String reservationId);
	
	ConfirmedReservation modifyReservation(String reservationId, RequestedReservation patch);
	
	ConfirmedReservation getReservation(String reservationId);

	void checkAvailability(List<LocalDate> dates);

	void validatingRequest(RequestedReservation request);

}
