package han.jiayun.campsite.reservation.availability;

import static han.jiayun.campsite.reservation.util.AvailabilityTool.isDateNotAvailable;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import han.jiayun.campsite.reservation.exceptions.DatesUnavailableException;
import han.jiayun.campsite.reservation.repositories.ReservationRepository;

/**
 * Checking whether the specified dates are available
 * @author Jiayun Han
 *
 */
@Service
public final class AvailabilityChecker {

	@Autowired
	private ReservationRepository reservationRepository;
	
	public void isUnavailable(List<LocalDate> dates) {
		
		for(LocalDate date : dates) {
			if(isDateNotAvailable(reservationRepository.reservations(), date)) {
				throw DatesUnavailableException.instance();
			}
		}
	}
}
