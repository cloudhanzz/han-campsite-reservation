package han.jiayun.campsite.reservation.service;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.RequestedReservation;

//TODO add context aware
public interface ReservationService {
	
	// return reservation ID
	String createReservation(RequestedReservation request);
	
	boolean cancelReservation(String reservationId);
	
	ConfirmedReservation modifyReservation(String reservationId);
	
	ConfirmedReservation getReservation(String reservationId);

}
