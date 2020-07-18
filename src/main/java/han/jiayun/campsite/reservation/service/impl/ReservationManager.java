package han.jiayun.campsite.reservation.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import han.jiayun.campsite.reservation.availability.AvailabilityManager;
import han.jiayun.campsite.reservation.exceptions.ReservationNotFoundException;
import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.repositories.ReservationRepository;
import han.jiayun.campsite.reservation.service.ReservationService;

@Service
public class ReservationManager implements ReservationService {
	
	private Map<String, ConfirmedReservation> repository;	
	private AvailabilityManager availabilityManager;	

	@Autowired
	public ReservationManager(ReservationRepository rp, AvailabilityManager availabilityManager) {
		this.repository = rp.reservations(); 
		this.availabilityManager = availabilityManager;
	}

	@Override
	public String createReservation(RequestedReservation requestedReservation) {
		
		ConfirmedReservation confirmedReservation = new ConfirmedReservation();
		
		confirmedReservation.setDates(requestedReservation.getDates());
		confirmedReservation.setUser(requestedReservation.getUser());
		confirmedReservation.setReservedAt(LocalDateTime.now());
		
		repository.put(confirmedReservation.getId(), confirmedReservation);
		
		return confirmedReservation.getId();
	}

	@Override
	public boolean cancelReservation(String reservationId) {
		ConfirmedReservation reservation = tryFindReservationById(reservationId);
		
		reservation.setCancelledAt(LocalDateTime.now());
		List<LocalDate> dates = availabilityManager.getDiscreteDates(reservation.getDates());
		availabilityManager.releaseDates(dates);
		
		// TODO policy: cancel do we delete it or simply mark it as cancelled?
		// TODO: add rule in application.properties
		// repository.remove(reservationId);
		
		return true;
	}

	@Override
	public ConfirmedReservation modifyReservation(String reservationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConfirmedReservation getReservation(String reservationId) {
		return tryFindReservationById(reservationId);
	}

	private ConfirmedReservation tryFindReservationById(String reservationId) {
		ConfirmedReservation reservation = repository.get(reservationId);		
		if(reservation == null || reservation.getCancelledAt() != null) {
			throw new ReservationNotFoundException(reservationId, "Please provide the correct reservation identifier");
		}
		return reservation;
	}
}
