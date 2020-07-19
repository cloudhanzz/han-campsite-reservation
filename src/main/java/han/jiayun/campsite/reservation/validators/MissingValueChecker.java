package han.jiayun.campsite.reservation.validators;

import java.util.List;

import han.jiayun.campsite.reservation.annotation.Required;
import han.jiayun.campsite.reservation.exceptions.MissRequiredValuesException;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.service.RequestValidator;
import han.jiayun.campsite.reservation.util.ReflectionTool;

/**
 * Checking missing of any required information. Limited to preliminary checking.
 * 
 * @author Jiayun Han
 *
 */
public final class MissingValueChecker implements RequestValidator {
    
    public void check(RequestedReservation request) {		
    	checkMissingFields(request);    	
    	checkMissingFields(request.getUser());    	
    	checkMissingFields(request.getDates());
	}

	private void checkMissingFields(Object request) {
		List<String> missingParts = ReflectionTool.getMissedAttributes(request, Required.class);
    	if(!missingParts.isEmpty()) {
    		throw new MissRequiredValuesException(missingParts);
    	}
	}
}
