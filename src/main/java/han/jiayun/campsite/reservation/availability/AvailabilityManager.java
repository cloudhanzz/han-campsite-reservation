package han.jiayun.campsite.reservation.availability;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import han.jiayun.campsite.reservation.model.DesiredDates;

/**
 * For (1) converting date range into consecutive dates
 * (2) Locking dates
 * (3) Releasing locked dates
 * @author cloud
 *
 */
public final class AvailabilityManager {

	private final Set<LocalDate> unavailableDates;

	public AvailabilityManager(Set<LocalDate> unavailableDates) {
		this.unavailableDates = unavailableDates;
	}

	public List<LocalDate> lockDatesAndRollbackOnException(List<LocalDate> dates) {

			try {
				dates.forEach(unavailableDates::add);
				return dates;
			} catch (Exception e) {
				releaseDates(dates);
				return new ArrayList<>();
			}
	}

	public List<LocalDate> getDiscreteDates(DesiredDates reservedDates) {
		List<LocalDate> dates = new ArrayList<>();

		LocalDate date = reservedDates.getArrival();
		LocalDate departureDate = reservedDates.getDeparture();

		do {
			dates.add(date);
			date = date.plusDays(1);
		} while (date.isBefore(departureDate));
		return dates;
	}
	
	public void releaseDates(List<LocalDate> dates) {
		dates.forEach(unavailableDates::remove);
	}

}
