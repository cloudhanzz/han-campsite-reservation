package han.jiayun.campsite.reservation.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import han.jiayun.campsite.reservation.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * The desired arrival and departure dates of a reservation
 * 
 * @author Jiayun Han
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class Schedule {
	
	@Required
	private LocalDate arrival;
	
	@Required
	private LocalDate departure;
	
	public List<LocalDate> toDiscreteDates() {
		List<LocalDate> dates = new ArrayList<>();

		LocalDate date = arrival;

		do {
			dates.add(date);
			date = date.plusDays(1);
		} while (date.isBefore(departure));
		return dates;
	}

}
