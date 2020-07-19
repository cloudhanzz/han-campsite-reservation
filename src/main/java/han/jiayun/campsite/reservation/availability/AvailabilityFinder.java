package han.jiayun.campsite.reservation.availability;

import static han.jiayun.campsite.reservation.util.AvailabilityTool.isDateAvailable;
import static han.jiayun.campsite.reservation.util.AvailabilityTool.isDateNotAvailable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import han.jiayun.campsite.reservation.model.FromTo;
import han.jiayun.campsite.reservation.repositories.ReservationRepository;

/**
 * For reporting available date ranges.
 * @author Jiayun Han
 *
 */
@Service
public class AvailabilityFinder {

	@Autowired
	private ReservationRepository reservationRepository;

	public List<FromTo> findAvailableDateRanges(Optional<LocalDate> optionalStart, Optional<LocalDate> optionalEnd) {

		LocalDate now = LocalDate.now();		
		LocalDate start = optionalStart.orElse(now.plusDays(1));
		LocalDate end = optionalEnd.orElse(now.plusMonths(1));		
		
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

		while (isDateNotAvailable(reservationRepository.reservations(), start) && !start.isEqual(end)) {
			start = start.plusDays(1);
		}

		if (isDateAvailable(reservationRepository.reservations(), start)) {
			return start;
		}

		return null;
	}

	private LocalDate findToDate(LocalDate start, LocalDate end) {

		LocalDate to = null;

		while (isDateAvailable(reservationRepository.reservations(), start) && !start.isAfter(end)) {
			to = start;
			start = start.plusDays(1);
		}

		return to;
	}
}
