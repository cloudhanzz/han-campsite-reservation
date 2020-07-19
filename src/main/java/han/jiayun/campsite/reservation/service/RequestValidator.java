package han.jiayun.campsite.reservation.service;

import han.jiayun.campsite.reservation.model.RequestedReservation;

@FunctionalInterface
public interface RequestValidator {
	
	void check(RequestedReservation request);

}
