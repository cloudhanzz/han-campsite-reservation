package han.jiayun.campsite.reservation.model;

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
	
	private UserInfo user;	
	private DesiredDates dates;
}
