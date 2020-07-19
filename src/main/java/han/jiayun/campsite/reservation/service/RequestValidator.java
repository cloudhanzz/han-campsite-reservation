package han.jiayun.campsite.reservation.service;

import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;

@FunctionalInterface
public interface RequestValidator {
	
	void check(UserInfo user, Schedule dates);

}
