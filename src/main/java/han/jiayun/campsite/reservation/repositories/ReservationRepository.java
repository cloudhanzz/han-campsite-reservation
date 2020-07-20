package han.jiayun.campsite.reservation.repositories;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import han.jiayun.campsite.reservation.exceptions.DatesUnavailableException;
import han.jiayun.campsite.reservation.exceptions.GenericServerException;
import han.jiayun.campsite.reservation.exceptions.ReservationNotFoundException;
import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.FromTo;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;
import han.jiayun.campsite.reservation.service.RequestValidator;

public enum ReservationRepository {
	
	INSTANCE;
	
	// reservationID -> reservations
	private final Map<String, ConfirmedReservation> reservations;
		
	private ReservationRepository() {
		this.reservations = new HashMap<>();
	}
	
	public synchronized void tryAddReservationWithDesiredDates(List<LocalDate> dates, ConfirmedReservation reservation) {
		
		for(LocalDate date : dates) {
			if(isDateNotAvailable(date)) {
				throw DatesUnavailableException.instance();
			}
		}
		
		reservations.put(reservation.getId(), reservation);		
	}
	
	public synchronized boolean isDateAvailable(LocalDate date) {
		return !(isDateNotAvailable(date));
	}
	
	public synchronized boolean isDateNotAvailable(LocalDate date) {
		List<LocalDate> datesAlreadyReserved = reservations.values().stream().map(r -> r.getDates())
				.flatMap(d -> d.toDiscreteDates().stream()).collect(toList());

		return datesAlreadyReserved.contains(date);
	}


	public synchronized ConfirmedReservation findReservation(String reservationId) {
		return Optional.ofNullable(reservations.get(reservationId)).orElseThrow(() -> new ReservationNotFoundException(reservationId));
	}


	public synchronized ConfirmedReservation cancelReservation(String reservationId) {
		return Optional.ofNullable(reservations.remove(reservationId)).orElseThrow(() -> new ReservationNotFoundException(reservationId));		
	}

	public synchronized ConfirmedReservation modifyReservation(ObjectMapper objMapper, List<RequestValidator> patchValidators, String reservationId, RequestedReservation patch) {
		
		ConfirmedReservation oldReservation = findReservation(reservationId);
		List<LocalDate> datesAlreadyOwned = oldReservation.getDates().toDiscreteDates();

		ConfirmedReservation newReservation = deepCloneConfirmedReservation(objMapper, oldReservation);
		integratePatch(patch, newReservation);

		validatePatchRequest(patchValidators, newReservation.getUser(), newReservation.getDates());
		validateNewDates(datesAlreadyOwned, newReservation);

		// Everything is fine when code come here
		newReservation.setModifiedAt(LocalDateTime.now());
		reservations.put(reservationId, newReservation);		

		return newReservation;
	}

	public synchronized List<FromTo> findAvailablePeriods(LocalDate start, LocalDate end) {
		
		List<FromTo> fromTos = new ArrayList<>();

		FromTo fromTo = findAvailablePeriod(start, end);
		if (fromTo != null) {
			fromTos.add(fromTo);

			start = fromTo.getTo().plusDays(1);
			while (start.isBefore(end)) {
				fromTo = findAvailablePeriod(start, end);
				if(fromTo == null) {
					break;
				}
				fromTos.add(fromTo);
				start = fromTo.getTo().plusDays(1);
			}
		}
		return fromTos;
	}

	public synchronized Set<String> getReservationIds() {
		return reservations.keySet();
	}


	private FromTo findAvailablePeriod(LocalDate start, LocalDate end) {
		LocalDate from = findFromDate(start, end);
		if (from == null) {
			return null;
		}

		LocalDate to = findToDate(from, end);
		FromTo fromTo = new FromTo();
		fromTo.setFrom(from);
		fromTo.setTo(to);

		return fromTo;
	}
	
	private LocalDate findFromDate(LocalDate start, LocalDate end) {

		while (isDateNotAvailable(start) && !start.isEqual(end)) {
			start = start.plusDays(1);
		}

		if (isDateAvailable(start)) {
			return start;
		}

		return null;
	}

	private LocalDate findToDate(LocalDate start, LocalDate end) {

		LocalDate to = null;

		while (isDateAvailable(start) && !start.isAfter(end)) {
			to = start;
			start = start.plusDays(1);
		}

		return to;
	}
	
	private ConfirmedReservation deepCloneConfirmedReservation(ObjectMapper objMapper, ConfirmedReservation oldReservation) {

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
	
	private void validateNewDates(List<LocalDate> datesAlreadyOwned, ConfirmedReservation newReservation) {

		List<LocalDate> attemptedDates = newReservation.getDates().toDiscreteDates();
		attemptedDates.removeAll(datesAlreadyOwned);

		for(LocalDate date : attemptedDates) {
			if(isDateNotAvailable(date)) {
				throw DatesUnavailableException.instance();
			}
		}
	}

	private void validatePatchRequest(List<RequestValidator> patchValidators, UserInfo user, Schedule dates) {
		patchValidators.forEach(validator -> validator.check(user, dates));		
	}
}
