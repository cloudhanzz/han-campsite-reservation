package han.jiayun.campsite.reservation.availability;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Checking whether the specified dates are available
 * @author Jiayun Han
 *
 */
public final class AvailabilityChecker {

	private final Set<LocalDate> unavailableDates;

	public AvailabilityChecker(Set<LocalDate> unavailableDates) {
		this.unavailableDates = unavailableDates;
	}
	
	public boolean isUnavailable(List<LocalDate> dates) {
		return unavailableDates.stream().anyMatch(dates::contains);
	}
}
