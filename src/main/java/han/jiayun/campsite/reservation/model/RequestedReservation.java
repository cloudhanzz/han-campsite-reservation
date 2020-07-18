package han.jiayun.campsite.reservation.model;

import han.jiayun.campsite.reservation.annotation.Required;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The reservation requested by a user
 * 
 * @author Jiayun Han
 */
@Getter
@Setter
@NoArgsConstructor
public class RequestedReservation {	
	
	@Required
	private UserInfo user;	
	
	@Required
	private DesiredDates dates;
}
