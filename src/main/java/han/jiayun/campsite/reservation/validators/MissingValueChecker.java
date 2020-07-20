package han.jiayun.campsite.reservation.validators;

import java.util.ArrayList;
import java.util.List;

import han.jiayun.campsite.reservation.annotation.Required;
import han.jiayun.campsite.reservation.exceptions.MissRequiredValuesException;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;
import han.jiayun.campsite.reservation.service.RequestValidator;
import han.jiayun.campsite.reservation.util.ReflectionTool;

/**
 * Checking missing of any required information. Limited to preliminary checking.
 * 
 * @author Jiayun Han
 *
 */
public final class MissingValueChecker implements RequestValidator {
    
    public void check(UserInfo user, Schedule dates) {	
    	
    	List<String> missingParts = new ArrayList<>();
    	if(user == null) {
    		missingParts.add("user");
    	} else {
    		missingParts.addAll(ReflectionTool.getMissedAttributes(user, Required.class));
    	}
    	
    	if(dates == null) {
    		missingParts.add("dates");
    	}else {
    		missingParts.addAll(ReflectionTool.getMissedAttributes(dates, Required.class));
    	}
    	
    	if(!missingParts.isEmpty()) {
    		throw new MissRequiredValuesException(missingParts);
    	}
	}
}
