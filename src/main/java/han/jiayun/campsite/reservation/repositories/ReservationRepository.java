package han.jiayun.campsite.reservation.repositories;

import java.util.concurrent.ConcurrentHashMap;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;

public enum ReservationRepository {
	
	INSTANCE;

	// reservationID -> reservations
	private final ConcurrentHashMap<String, ConfirmedReservation> reservations;
	
	private ReservationRepository() {
		this.reservations = new ConcurrentHashMap<>();
	}
	
	public ConcurrentHashMap<String, ConfirmedReservation> reservations() {
		return reservations;
	}
}
