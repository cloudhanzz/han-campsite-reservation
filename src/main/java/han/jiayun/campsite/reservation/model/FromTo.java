package han.jiayun.campsite.reservation.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * Modeling a date range available for reservation
 * 
 * @author Jiayun Han
 *
 */
@Getter
@Setter
public class FromTo {

	private LocalDate from;
	private LocalDate to;
}
