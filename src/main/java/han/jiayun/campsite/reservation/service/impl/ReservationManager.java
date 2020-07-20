package han.jiayun.campsite.reservation.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import han.jiayun.campsite.reservation.exceptions.InvalidDateRangeException;
import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.FromTo;
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
	private ValidatingService validatingService;

	@Autowired
	private ObjectMapper objMapper;

	@Override
	public String createReservation(RequestedReservation request) {

		ConfirmedReservation reservation = new ConfirmedReservation();

		reservation.setDates(request.getDates());
		reservation.setUser(request.getUser());
		reservation.setReservedAt(LocalDateTime.now());
		List<LocalDate> individualDates = request.getDates().toDiscreteDates();

		String id = reservation.getId();
		
		ReservationRepository.INSTANCE.tryAddReservationWithDesiredDates(individualDates, reservation);

		return id;
	}

	@Override
	public void cancelReservation(String reservationId) {
		ReservationRepository.INSTANCE.cancelReservation(reservationId);
	}

	@Override
	public ConfirmedReservation modifyReservation(String reservationId, RequestedReservation patch) {		

		return ReservationRepository.INSTANCE.modifyReservation(objMapper, validatingService.patchValidators(), reservationId, patch);
	}

	@Override
	public ConfirmedReservation getReservation(String reservationId) {
		return ReservationRepository.INSTANCE.findReservation(reservationId);
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
	public List<FromTo> findAvailableDateRanges(Optional<LocalDate> optionalStart, Optional<LocalDate> optionalEnd) {
		LocalDate now = LocalDate.now();		
		LocalDate start = optionalStart.orElse(now.plusDays(1));
		LocalDate end = optionalEnd.orElse(now.plusMonths(1));	
		
		if(start.isAfter(end)) {
			throw InvalidDateRangeException.instance();
		}		
		
		return ReservationRepository.INSTANCE.findAvailablePeriods(start, end);
	}
}
