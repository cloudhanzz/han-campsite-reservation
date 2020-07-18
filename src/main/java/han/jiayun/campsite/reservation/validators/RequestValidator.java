package han.jiayun.campsite.reservation.validators;

import han.jiayun.campsite.reservation.model.RequestedReservation;

//TODO functional interface?
public interface RequestValidator {
	
	void check(RequestedReservation request);

}
