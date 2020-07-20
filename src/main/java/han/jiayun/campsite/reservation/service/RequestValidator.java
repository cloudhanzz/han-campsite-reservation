package han.jiayun.campsite.reservation.service;

import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;

/**
 * 
 * @author Jiayun Han
 *
 */
public interface RequestValidator {
	
	default void check(RequestedReservation request) {
		check(request.getUser(), request.getDates());
	}
	
	void check(UserInfo user, Schedule dates);
}
