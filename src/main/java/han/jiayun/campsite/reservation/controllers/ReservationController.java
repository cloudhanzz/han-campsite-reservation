package han.jiayun.campsite.reservation.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.util.StubFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Jiayun Han
 *
 */
@RestController
@RequestMapping(value = "/camping/v1.0/reservations", produces = "application/json")
public class ReservationController {

	@GetMapping("/available/dates")
	@ApiOperation(value = "Find available dates of a range", response = ResponseEntity.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 406, message = "Invalid date range"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation") })
	public ResponseEntity<?> getAvailableDates(@RequestParam Optional<String> from, @RequestParam Optional<String> to) {
		return ResponseEntity.ok().body(StubFactory.OPEN_DATES);
	}
	
	@PostMapping
	@ApiOperation(value = "Create a new reservation", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 406, message = "Not Acceptable request body, such as arrival date is not after the departure date"),
			@ApiResponse(code = 409, message = "Conflict: the attempted dates not available"),
			@ApiResponse(code = 422, message = "Missing required value(s)"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation")
	})
	public ResponseEntity<?> makeReservation(@RequestBody RequestedReservation request) {
		return ResponseEntity
				.created(URI.create("http://localhost:9999/reservations/bd23951e-deab-4227-b42f-157392ba2fcf"))
				.body(StubFactory.ORIGINAL_RESERVATION);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Find reservation by its identifier", response = ResponseEntity.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Reservation not found"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation") })
	public ResponseEntity<?> getReservation(@PathVariable String id) {
		return ResponseEntity.ok(StubFactory.ORIGINAL_RESERVATION);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Cancel a reservation", response = ResponseEntity.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "Success with no content"),
			@ApiResponse(code = 404, message = "Reservation"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation") })
	public ResponseEntity<?> cancelReservation(@PathVariable String id) {
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}")
	@ApiOperation(value = "Update the reservation with the provided details. Not-mentioned aspects won't be modified", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Success with no content"),
			@ApiResponse(code = 404, message = "Reservation not found"),
			@ApiResponse(code = 406, message = "Not Acceptable request body, such as arrival date is not after the departure date"),
			@ApiResponse(code = 422, message = "Missing required value(s)"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation")
	})
	public ResponseEntity<?> modifyReservation(@PathVariable String id, @RequestBody RequestedReservation request) {
		return ResponseEntity.noContent().build();
	}
}
