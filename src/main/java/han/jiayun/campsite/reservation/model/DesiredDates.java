package han.jiayun.campsite.reservation.model;

import java.time.LocalDate;

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
	private LocalDate arrival;
	private LocalDate departure;

}
