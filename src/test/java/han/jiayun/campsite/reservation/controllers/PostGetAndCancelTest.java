package han.jiayun.campsite.reservation.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import han.jiayun.campsite.reservation.model.CreationResponse;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;
import han.jiayun.campsite.reservation.repositories.ReservationRepository;

/**
 * 
 * @author Jiayun Han
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test getting, and cancelling reservations")
public class PostGetAndCancelTest extends ControllerTestParent {

	private static final UserInfo USER = new UserInfo("John", "Doe", "jiayunhan@gmail.com");

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	public void cancelExistingReservations() throws Exception {
		List<String> ids = Collections.list(ReservationRepository.INSTANCE.reservations().keys());
		for(String id : ids) {
			mvc.perform(delete("/camping/v1.0/reservations/" + id));
		}
	}

	@Test
	@DisplayName("Test getting an nonexistent reservation and return error")
	public void get_nonexisting_reservation() throws Exception {
		mvc.perform(get("/camping/v1.0/reservations/bogus-non-existing-reservation-id"))
		.andExpect(status().isNotFound());
	}
		
	@Test
	@DisplayName("Test getting a created reservation")
	public void getting_created_reservation() throws Exception {
		
        LocalDate today = LocalDate.now();
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = arrival.plusDays(2);
		Schedule dates = new Schedule(arrival, departure);
		RequestedReservation request = new RequestedReservation();

		request.setUser(USER);
		request.setDates(dates);

		// Create reservation
		String result = mvc.perform(post("/camping/v1.0/reservations").content(objectMapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		
		CreationResponse response = objectMapper.readValue(result, CreationResponse.class);		
		String reservationId = response.getReservationId();
		
		// getting created reservation
		mvc.perform(get("/camping/v1.0/reservations/" + reservationId))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.user.firstName", is("John")))
		.andExpect(jsonPath("$.user.lastName", is("Doe")));
	}
		
	@Test
	@DisplayName("Test canceled reservation cannot be found")
	public void cancelled_reservation_cannot_be_found() throws Exception {

		LocalDate today = LocalDate.now();;
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = arrival.plusDays(2);
		Schedule dates = new Schedule(arrival, departure);
		RequestedReservation request = new RequestedReservation();

		request.setUser(USER);
		request.setDates(dates);

		// Create reservation
		String result = mvc.perform(post("/camping/v1.0/reservations").content(objectMapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		
		CreationResponse response = objectMapper.readValue(result, CreationResponse.class);		
		String reservationId = response.getReservationId();
		
		// Get reservation returns OK
		mvc.perform(get("/camping/v1.0/reservations/" + reservationId))
		.andExpect(status().isOk());
		
		// Cancel reservation
		mvc.perform(delete("/camping/v1.0/reservations/" + reservationId));
		
		// cannot retrieve reservation any longer
		mvc.perform(get("/camping/v1.0/reservations/" + reservationId))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Test reserved dates are not available after reservation")
	public void dates_unavailable_after_reservation() throws Exception {

		LocalDate today = LocalDate.now();;
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = arrival.plusDays(2);
		
		String fromDate = arrival.format(DATETIME_FORMATTER);
		String toDate = departure.minusDays(1).format(DATETIME_FORMATTER);
		
		Schedule dates = new Schedule(arrival, departure);
		RequestedReservation request = new RequestedReservation();

		request.setUser(USER);
		request.setDates(dates);

		// Create reservation
		 mvc.perform(post("/camping/v1.0/reservations").content(objectMapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		 
		// Check dates not available
		 mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", fromDate).queryParam("to", toDate))
		 .andExpect(jsonPath("$", hasSize(0)));		 
	}
	
	@Test
	@DisplayName("Test reserved dates are available again after reservation is cancelled")
	public void dates_available_after_reservation_cancelled() throws Exception {

		LocalDate today = LocalDate.now();;
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = arrival.plusDays(2);
		
		String fromDate = arrival.format(DATETIME_FORMATTER);
		String toDate = departure.minusDays(1).format(DATETIME_FORMATTER);
		
		Schedule dates = new Schedule(arrival, departure);
		RequestedReservation request = new RequestedReservation();

		request.setUser(USER);
		request.setDates(dates);

		// Create reservation
		String result = mvc.perform(post("/camping/v1.0/reservations").content(objectMapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		
		CreationResponse response = objectMapper.readValue(result, CreationResponse.class);		
		String reservationId = response.getReservationId();
		 
		// Check dates not available
		 mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", fromDate).queryParam("to", toDate))
		 .andExpect(jsonPath("$", hasSize(0)));
		 
		// Cancel reservation
		mvc.perform(delete("/camping/v1.0/reservations/" + reservationId));

		// Check dates become available again
		 mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", fromDate).queryParam("to", toDate))
		 .andExpect(jsonPath("$", hasSize(1)));		 
	}
}
