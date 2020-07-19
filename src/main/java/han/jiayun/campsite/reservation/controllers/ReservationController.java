package han.jiayun.campsite.reservation.controllers;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import han.jiayun.campsite.reservation.availability.AvailabilityFinder;
import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.CreationResponse;
import han.jiayun.campsite.reservation.model.FromTo;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.service.ReservationService;
import han.jiayun.campsite.reservation.util.Messages;
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

	@Autowired
	private AvailabilityFinder availabilityFinder;

	@Autowired
	private ReservationService reservationService;

	@GetMapping("/available/dates")
	@ApiOperation(value = "Find available dates of a range", response = FromTo.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 406, message = "Invalid date range"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation") })
	public List<FromTo> getAvailableDates(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> from,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> to) {
		return availabilityFinder.findAvailableDateRanges(from, to);
	}

	@PostMapping
	@ApiOperation(value = "Create a new reservation", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 406, message = "Not Acceptable request body, such as arrival date is not after the departure date"),
			@ApiResponse(code = 409, message = "Conflict: the attempted dates not available"),
			@ApiResponse(code = 422, message = "Missing required value(s)"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation") })
	public ResponseEntity<CreationResponse> makeReservation(@RequestBody RequestedReservation request) {

		reservationService.validatingRequest(request);
		reservationService.checkAvailability(request.getDates().toDiscreteDates());

		String reservationId = reservationService.createReservation(request);
		URI location = buildLocation(reservationId);

		CreationResponse body = new CreationResponse(reservationId, HttpStatus.CREATED.toString(),
				Messages.AFTER_MAKING_RESERVATION);

		return ResponseEntity.created(location).body(body);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Find reservation by its identifier", response = ConfirmedReservation.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Reservation not found"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation") })
	public ConfirmedReservation getReservation(@PathVariable String id) {
		return reservationService.getReservation(id);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Cancel a reservation", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Success with no content"),
			@ApiResponse(code = 404, message = "Reservation"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation") })
	public ResponseEntity<?> cancelReservation(@PathVariable String id) {
		if (reservationService.cancelReservation(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@PatchMapping("/{id}")
	@ApiOperation(value = "Update the reservation with the provided details. Not-mentioned aspects won't be modified", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Success with no content"),
			@ApiResponse(code = 404, message = "Reservation not found"),
			@ApiResponse(code = 406, message = "Not Acceptable request body, such as arrival date is not after the departure date"),
			@ApiResponse(code = 422, message = "Missing required value(s)"),
			@ApiResponse(code = 500, message = "Server failed to perform the operation") })
	public ResponseEntity<ConfirmedReservation> modifyReservation(@PathVariable String id, @RequestBody RequestedReservation request) {
		ConfirmedReservation r = reservationService.modifyReservation(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(r);
	}

	private URI buildLocation(String reservationId) {

		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(reservationId).toUri();
	}
}
