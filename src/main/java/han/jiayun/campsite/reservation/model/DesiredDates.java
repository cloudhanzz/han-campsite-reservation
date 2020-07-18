package han.jiayun.campsite.reservation.model;

import java.time.LocalDate;

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
public class DesiredDates {
	
	@Required
	private LocalDate arrival;
	
	@Required
	private LocalDate departure;

}
