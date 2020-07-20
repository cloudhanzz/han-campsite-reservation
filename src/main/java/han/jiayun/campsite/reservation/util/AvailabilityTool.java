/**
 * 
 */
package han.jiayun.campsite.reservation.util;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;

/**
 * @author Jiayun Han
 *
 */
public final class AvailabilityTool {

	private AvailabilityTool() {
	}	

	public static boolean isDateAvailable(Map<String, ConfirmedReservation> reservations, LocalDate date) {
		return !(isDateNotAvailable(reservations, date));
	}

	public static boolean isDateNotAvailable(Map<String, ConfirmedReservation> reservations, LocalDate date) {
		
		List<LocalDate> list = reservations
				.values()
				.stream()
				.map(r -> r.getDates())
				.flatMap(d -> d.toDiscreteDates().stream()).collect(toList());
		
		return list.contains(date);
	}

}
