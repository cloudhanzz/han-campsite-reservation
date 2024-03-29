package han.jiayun.campsite.reservation.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import han.jiayun.campsite.reservation.service.RequestValidator;
import han.jiayun.campsite.reservation.service.ValidatingService;
import han.jiayun.campsite.reservation.validators.ArrivalDateChecker;
import han.jiayun.campsite.reservation.validators.MissingValueChecker;
import han.jiayun.campsite.reservation.validators.ReservationLengthChecker;
import han.jiayun.campsite.reservation.validators.UserInfoChecker;

/**
 * 
 * @author Jiayun Han
 */
@Configuration
public class ProjectConfig {
	
	@Bean
	public ValidatingService validatingService() {
		return new ValidatingService() {
			
			@Override
			public List<RequestValidator> postValidators() {
				List<RequestValidator> checkers = new ArrayList<>();
				checkers.add(new MissingValueChecker());
				checkers.add(new UserInfoChecker());
				checkers.add(new ArrivalDateChecker());
				checkers.add(new ReservationLengthChecker());
				return checkers;
			}
			
			@Override
			public List<RequestValidator> patchValidators() {
				List<RequestValidator> checkers = new ArrayList<>();
				checkers.add(new UserInfoChecker());
				checkers.add(new ArrivalDateChecker());
				checkers.add(new ReservationLengthChecker());
				return checkers;
			}
		};
	}
}
