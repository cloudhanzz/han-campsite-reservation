package han.jiayun.campsite.reservation.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import han.jiayun.campsite.reservation.service.ReservationService;
import han.jiayun.campsite.reservation.service.ValidatingService;

/**
 * 
 * @author Jiayun Han
 *
 */
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

		ConfirmedReservation newReservation = deepCloneConfirmedReservation(oldReservation);
		integratePatch(patch, newReservation);
		
		validatePatchRequest(newReservation.getUser(), newReservation.getDates());
		validateNewDates(datesAlreadyOwned, newReservation);

		// Everything is fine when code come here
		newReservation.setModifiedAt(LocalDateTime.now());
		ReservationRepository.INSTANCE.reservations().put(reservationId, newReservation);

		return newReservation;
	}

	@Override
	public ConfirmedReservation getReservation(String reservationId) {
		return tryFindReservationById(reservationId);
	}

	@Override
	public void validatePostRequest(RequestedReservation request) {
		validatingService.postValidators().forEach(validator -> validator.check(request.getUser(), request.getDates()));
	}

	@Override
	public void validatePatchRequest(UserInfo user, Schedule dates) {
		validatingService.patchValidators().forEach(validator -> validator.check(user, dates));
	}	
	

	@Override
	public void checkAvailability(List<LocalDate> dates) {
		availabilityChecker.check(dates);

	}

	private void validateNewDates(List<LocalDate> datesAlreadyOwned, ConfirmedReservation newReservation) {
		
		List<LocalDate> attemptedDates = newReservation.getDates().toDiscreteDates();
		attemptedDates.removeAll(datesAlreadyOwned);
		
		checkAvailability(attemptedDates);
	}

	// To prevent unwanted changes left in confirmed reservation upon unsuccessful
	// modification.
	private ConfirmedReservation deepCloneConfirmedReservation(ConfirmedReservation oldReservation) {

		ConfirmedReservation newReservation = null;
		try {
			newReservation = objMapper.readValue(objMapper.writeValueAsString(oldReservation),
					ConfirmedReservation.class);
		} catch (JsonProcessingException e) {
			throw new GenericServerException("Reservation cannot be modified now. Please try it later.");
		}
		return newReservation;
	}

	private void integratePatch(RequestedReservation patch, ConfirmedReservation newReservation) {
		
		integrateUserPatch(patch, newReservation);
		integrateSchedulePatch(patch, newReservation);
	}

	private void integrateUserPatch(RequestedReservation patch, ConfirmedReservation newReservation) {
		UserInfo newUserInfo = patch.getUser();
		if (newUserInfo != null) {
			if (newUserInfo.getFirstName() != null) {
				newReservation.getUser().setFirstName(newUserInfo.getFirstName());
			}
			if (newUserInfo.getLastName() != null) {
				newReservation.getUser().setLastName(newUserInfo.getLastName());
			}
			if (newUserInfo.getEmail() != null) {
				newReservation.getUser().setEmail(newUserInfo.getEmail());
			}
		}
	}

	private void integrateSchedulePatch(RequestedReservation patch, ConfirmedReservation newReservation) {
		Schedule newDates = patch.getDates();
		if (newDates != null) {
			if (newDates.getArrival() != null) {
				newReservation.getDates().setArrival(newDates.getArrival());
			}

			if (newDates.getDeparture() != null) {
				newReservation.getDates().setDeparture(newDates.getDeparture());
			}
		}
	}

	private ConfirmedReservation tryFindReservationById(String reservationId) {
		ConfirmedReservation reservation = ReservationRepository.INSTANCE.reservations().get(reservationId);
		if (reservation == null || reservation.getCancelledAt() != null) {
			throw new ReservationNotFoundException(reservationId, "Please provide the correct reservation identifier");
		}
		return reservation;
	}
}
