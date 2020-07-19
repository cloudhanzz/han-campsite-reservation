package han.jiayun.campsite.reservation.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import han.jiayun.campsite.reservation.availability.AvailabilityChecker;
import han.jiayun.campsite.reservation.exceptions.GenericServerException;
import han.jiayun.campsite.reservation.exceptions.ReservationNotFoundException;
import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;
import han.jiayun.campsite.reservation.repositories.ReservationRepository;
import han.jiayun.campsite.reservation.service.RequestValidator;
import han.jiayun.campsite.reservation.service.ReservationService;
import han.jiayun.campsite.reservation.service.ValidatingService;
import han.jiayun.campsite.reservation.validators.ArrivalDateChecker;
import han.jiayun.campsite.reservation.validators.ReservationLengthChecker;
import han.jiayun.campsite.reservation.validators.UserInfoChecker;

@Service
public class ReservationManager implements ReservationService {

	@Autowired
	private AvailabilityChecker availabilityChecker;

	@Autowired
	private ValidatingService validatingService;
	
	@Autowired
	private ObjectMapper objMapper;
	
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
	public ConfirmedReservation modifyReservation(String reservationId, RequestedReservation patch) {
		
		ConfirmedReservation oldReservation = tryFindReservationById(reservationId);
		List<LocalDate> datesAlreadyOwned = oldReservation.getDates().toDiscreteDates();
		
		// Deep clone reservation from repository
		ConfirmedReservation newReservation = null;
		try {
			newReservation = objMapper.readValue(objMapper.writeValueAsString(oldReservation), ConfirmedReservation.class);
		} catch (JsonProcessingException e) {
			throw new GenericServerException("Reservation cannot be modified now. Please try it later.");
		}
		
		// Integrate information from request
		UserInfo newUserInfo = patch.getUser();
		if(newUserInfo != null) {
			if(newUserInfo.getFirstName() != null) {
				newReservation.getUser().setFirstName(newUserInfo.getFirstName());
			}
			if(newUserInfo.getLastName() != null) {
				newReservation.getUser().setLastName(newUserInfo.getLastName());
			}
			if(newUserInfo.getEmail() != null) {
				newReservation.getUser().setEmail(newUserInfo.getEmail());
			}
		}
		
		Schedule d2 = patch.getDates();
		if(d2 != null) {
			if(d2.getArrival() != null) {
				newReservation.getDates().setArrival(d2.getArrival());
			}

			if(d2.getDeparture() != null) {
				newReservation.getDates().setDeparture(d2.getDeparture());
			}
		}
				
		List<RequestValidator> checkers = new ArrayList<>();
		checkers.add(new UserInfoChecker());
		checkers.add(new ArrivalDateChecker());
		checkers.add(new ReservationLengthChecker());
		
		for(RequestValidator checker : checkers) {
			checker.check(newReservation.getUser(), newReservation.getDates());
		}
		
		List<LocalDate> attemptedDates = newReservation.getDates().toDiscreteDates();
		
		attemptedDates.removeAll(datesAlreadyOwned);
		checkAvailability(attemptedDates);
		
		// Everything is fine here
		newReservation.setModifiedAt(LocalDateTime.now());
		ReservationRepository.INSTANCE.reservations().put(reservationId, newReservation);		

		return newReservation;
	}

	@Override
	public ConfirmedReservation getReservation(String reservationId) {
		return tryFindReservationById(reservationId);
	}
	
    @Override
	public void validatingRequest(RequestedReservation request) {
		validatingService.validators().forEach(validator -> validator.check(request.getUser(), request.getDates()));

	}
	
	@Override
	public void checkAvailability(List<LocalDate> dates) {
		availabilityChecker.check(dates);

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
