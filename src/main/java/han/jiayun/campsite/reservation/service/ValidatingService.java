package han.jiayun.campsite.reservation.service;

import java.util.List;

/**
 * 
 * @author Jiayun Han
 *
 */
public interface ValidatingService {
	
	List<RequestValidator> postValidators();	
	List<RequestValidator> patchValidators();

}
