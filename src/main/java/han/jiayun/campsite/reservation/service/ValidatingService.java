package han.jiayun.campsite.reservation.service;

import java.util.List;

@FunctionalInterface
public interface ValidatingService {
	
	List<RequestValidator> validators();

}
