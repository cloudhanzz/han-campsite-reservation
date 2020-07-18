package han.jiayun.campsite.reservation.model;

import java.time.LocalDate;

import han.jiayun.campsite.reservation.annotation.Required;
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

	@Required
	private LocalDate from;
	
	@Required
	private LocalDate to;
}
