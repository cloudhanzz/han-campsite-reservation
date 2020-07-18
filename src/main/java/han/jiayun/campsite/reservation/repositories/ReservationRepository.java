package han.jiayun.campsite.reservation.repositories;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;

public interface ReservationRepository {
	
	Set<LocalDate> unavailableDates();
	Map<String, ConfirmedReservation> reservations();

}
