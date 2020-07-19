package han.jiayun.campsite.reservation.repositories;

import java.util.Map;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;

@FunctionalInterface
public interface ReservationRepository {	
	Map<String, ConfirmedReservation> reservations();
}
