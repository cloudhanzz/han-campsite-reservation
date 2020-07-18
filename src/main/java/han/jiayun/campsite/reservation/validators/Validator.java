package han.jiayun.campsite.reservation.validators;

import han.jiayun.campsite.reservation.model.RequestedReservation;

//TODO functional interface?
public interface Validator {
	
	void check(RequestedReservation request);

}
