package han.jiayun.campsite.reservation.config;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import han.jiayun.campsite.reservation.availability.AvailabilityChecker;
import han.jiayun.campsite.reservation.availability.AvailabilityFinder;
import han.jiayun.campsite.reservation.availability.AvailabilityManager;
import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.repositories.ReservationRepository;

/**
 * 
 * @author Jiayun Han
 */
@Configuration
public class ProjectConfig {
		
	@Bean
	public ReservationRepository reservationRepository() {
		return new ReservationRepository() {
			
			@Override
			public Set<LocalDate> unavailableDates() {
				return ConcurrentHashMap.newKeySet();
			}
			
			@Override
			public Map<String, ConfirmedReservation> reservations() {
				return new ConcurrentHashMap<>();
			}
		};
	}
	
	@Bean
	public AvailabilityFinder availablityFinder() {
		return new AvailabilityFinder(reservationRepository().unavailableDates());
	}
	
	@Bean
	public AvailabilityManager availablityManager() {
		return new AvailabilityManager(reservationRepository().unavailableDates());
	}
	
	@Bean
	public AvailabilityChecker availablityChecker() {
		return new AvailabilityChecker(reservationRepository().unavailableDates());
	}
}
