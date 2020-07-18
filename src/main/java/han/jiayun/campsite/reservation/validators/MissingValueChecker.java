package han.jiayun.campsite.reservation.validators;

import java.util.List;

import han.jiayun.campsite.reservation.annotation.Required;
import han.jiayun.campsite.reservation.exceptions.MissRequiredValuesException;
import han.jiayun.campsite.reservation.model.DesiredDates;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.UserInfo;
import han.jiayun.campsite.reservation.util.ReflectionTool;

/**
 * Checking missing of any required information. Limited to preliminary checking.
 * 
 * @author Jiayun Han
 *
 */
public final class MissingValueChecker {
    
    public void preCheck(RequestedReservation request) {
		
    	checkMissingFields(request);    	
    	checkUser(request.getUser());    	
    	checkDates(request.getDates());
	}
	
	public void checkUser(UserInfo user) {		
		checkMissingFields(user);
	}
	
    public void checkDates(DesiredDates dates) {
    	checkMissingFields(dates);
	}

	private void checkMissingFields(Object request) {
		List<String> missingParts = ReflectionTool.getMissedAttributes(request, Required.class);
    	if(!missingParts.isEmpty()) {
    		throw new MissRequiredValuesException(missingParts);
    	}
	}
}
