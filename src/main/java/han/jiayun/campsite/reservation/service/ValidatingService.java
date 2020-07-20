package han.jiayun.campsite.reservation.service;

import java.util.List;

public interface ValidatingService {
	
	List<RequestValidator> postValidators();	
	List<RequestValidator> patchValidators();

}
