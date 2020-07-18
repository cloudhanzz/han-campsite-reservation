package han.jiayun.campsite.reservation.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.DesiredDates;
import han.jiayun.campsite.reservation.model.FromTo;
import han.jiayun.campsite.reservation.model.RequestedReservation;

/**
 * @author Jiayun Han
 *
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController {
	
	@GetMapping("/available-dates")
	public List<FromTo> getAvailableDates() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	@PostMapping
	public ConfirmedReservation makeReservation(@RequestBody RequestedReservation request) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	@GetMapping("/{id}")
	public ConfirmedReservation getReservation(@PathVariable String id) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> cancelReservation(@PathVariable String id) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	@PatchMapping("/{id}")
	public ConfirmedReservation modifyReservation(@PathVariable String id, @RequestBody DesiredDates newDates) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
