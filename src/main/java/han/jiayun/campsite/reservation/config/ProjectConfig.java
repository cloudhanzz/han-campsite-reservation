package han.jiayun.campsite.reservation.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import han.jiayun.campsite.reservation.repositories.ReservationRepository;

/**
 * 
 * @author Jiayun Han
 */
@Configuration
public class ProjectConfig {
		
	@Bean
	public ReservationRepository reservationRepository() {
		return () -> new ConcurrentHashMap<>();
	}
}
