package han.jiayun.campsite.reservation.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import han.jiayun.campsite.reservation.exceptions.ReservationNotFoundException;
import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.repositories.ReservationRepository;
import han.jiayun.campsite.reservation.service.ReservationService;

@Service
public class ReservationManager implements ReservationService {

	@Override
	public String createReservation(RequestedReservation request) {

		ConfirmedReservation reservation = new ConfirmedReservation();

		reservation.setDates(request.getDates());
		reservation.setUser(request.getUser());
		reservation.setReservedAt(LocalDateTime.now());

		String id = reservation.getId();
		ReservationRepository.INSTANCE.reservations().put(id, reservation);

		return id;
	}

	@Override
	public boolean cancelReservation(String reservationId) {
		
		ConfirmedReservation reservation = tryFindReservationById(reservationId);
		
		try {
			reservation.setCancelledAt(LocalDateTime.now());
		} catch (Exception e) {
			return false;
		}
		
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

//	private Map<String, ConfirmedReservation> repository;	
//
//	@Autowired
//	public ReservationManager(ReservationRepository rp, AvailabilityManager availabilityManager) {
//		this.repository = rp.reservations(); 
//		this.availabilityManager = availabilityManager;
//	}
//
//	@Override
//	public String createReservation(RequestedReservation requestedReservation) {
//		
//		ConfirmedReservation confirmedReservation = new ConfirmedReservation();
//		
//		confirmedReservation.setDates(requestedReservation.getDates());
//		confirmedReservation.setUser(requestedReservation.getUser());
//		confirmedReservation.setReservedAt(LocalDateTime.now());
//		
//		repository.put(confirmedReservation.getId(), confirmedReservation);
//		
//		return confirmedReservation.getId();
//	}
//
//	@Override
//	public boolean cancelReservation(String reservationId) {
//		ConfirmedReservation reservation = tryFindReservationById(reservationId);
//		
//		reservation.setCancelledAt(LocalDateTime.now());
//		List<LocalDate> dates = availabilityManager.getDiscreteDates(reservation.getDates());
//		availabilityManager.releaseDates(dates);
//		
//		// TODO policy: cancel do we delete it or simply mark it as cancelled?
//		// TODO: add rule in application.properties
//		// repository.remove(reservationId);
//		
//		return true;
//	}
//
//	@Override
//	public ConfirmedReservation modifyReservation(String reservationId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ConfirmedReservation getReservation(String reservationId) {
//		return tryFindReservationById(reservationId);
//	}
//
	private ConfirmedReservation tryFindReservationById(String reservationId) {
		ConfirmedReservation reservation = ReservationRepository.INSTANCE.reservations().get(reservationId);
		if (reservation == null || reservation.getCancelledAt() != null) {
			throw new ReservationNotFoundException(reservationId, "Please provide the correct reservation identifier");
		}
		return reservation;
	}
}
