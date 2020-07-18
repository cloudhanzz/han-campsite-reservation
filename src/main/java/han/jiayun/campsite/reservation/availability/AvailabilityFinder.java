package han.jiayun.campsite.reservation.availability;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import han.jiayun.campsite.reservation.model.FromTo;

/**
 * For reporting available date ranges.
 * @author Jiayun Han
 *
 */
public final class AvailabilityFinder {

	private final Set<LocalDate> unavailableDates;

	public AvailabilityFinder() {
		this(new HashSet<>());
	}

	public AvailabilityFinder(Set<LocalDate> unavailableDates) {
		this.unavailableDates = unavailableDates;
	}

	public List<FromTo> findAvailableDateRanges() {

		List<FromTo> fromTos = new ArrayList<>();

		LocalDate now = LocalDate.now();
		LocalDate start = now.plusDays(1);
		LocalDate end = now.plusMonths(1);

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

		while (!isDateAvailable(start) && !start.isEqual(end)) {
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

	private boolean isDateAvailable(LocalDate date) {
		return !unavailableDates.contains(date);
	}

}
