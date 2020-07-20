package han.jiayun.campsite.reservation.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
public class PatchTest extends ControllerTestParent {

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
	@DisplayName("Test modifying user info")
	public void modify_user_info() throws Exception {
			
        LocalDate today = LocalDate.now();
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = arrival.plusDays(2);
		Schedule dates = new Schedule(arrival, departure);
		RequestedReservation request = new RequestedReservation();

		UserInfo john = new UserInfo("John", "Doe", "joh.doe@gmail.com");
		UserInfo mike = new UserInfo("Mike", "Lee", "mike.lee@gmail.com");
		
		request.setUser(john);
		request.setDates(dates);

		// Create reservation
		String result = mvc.perform(post("/camping/v1.0/reservations")
				.content(objectMapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		
		CreationResponse response = objectMapper.readValue(result, CreationResponse.class);		
		String reservationId = response.getReservationId();
		
		// getting created reservation
		mvc.perform(get("/camping/v1.0/reservations/" + reservationId))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.user.firstName", is(john.getFirstName())))
		.andExpect(jsonPath("$.user.lastName", is(john.getLastName())))
		.andExpect(jsonPath("$.user.email", is(john.getEmail())));
		
		// modify reservation
		RequestedReservation patch = new RequestedReservation();
		patch.setUser(mike);
		
		mvc.perform(patch("/camping/v1.0/reservations/" + reservationId)
				.content(objectMapper.writeValueAsString(patch))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.user.firstName", is(mike.getFirstName())))
		.andExpect(jsonPath("$.user.lastName", is(mike.getLastName())))
		.andExpect(jsonPath("$.user.email", is(mike.getEmail())));
	}
		


	@Test
	@DisplayName("Test modifying arrival and departure dates")
	public void modify_dates_info() throws Exception {
			
        LocalDate today = LocalDate.now();
        
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = arrival.plusDays(2);
		String fromDate = arrival.format(formatter);
		String departureDate = departure.format(formatter);
		String toDate = departure.minusDays(1).format(formatter);
		

		LocalDate newArrival = today.plusDays(10);
		LocalDate newDeparture = newArrival.plusDays(2);
		String newFromDate = newArrival.format(formatter);
		String newDepartureDate = newDeparture.format(formatter);
		String newToDate = newDeparture.minusDays(1).format(formatter);
		Schedule newDates = new Schedule(newArrival, newDeparture);
		
		
		Schedule dates = new Schedule(arrival, departure);
		RequestedReservation request = new RequestedReservation();

		UserInfo john = new UserInfo("John", "Doe", "joh.doe@gmail.com");
		
		request.setUser(john);
		request.setDates(dates);

		// Create reservation
		String result = mvc.perform(post("/camping/v1.0/reservations")
				.content(objectMapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();
		
		CreationResponse response = objectMapper.readValue(result, CreationResponse.class);		
		String reservationId = response.getReservationId();
		
		// getting created reservation
		mvc.perform(get("/camping/v1.0/reservations/" + reservationId))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.dates.arrival", is(fromDate)))
		.andExpect(jsonPath("$.dates.departure", is(departureDate)));
		
		// verify reserved dates are not available
		mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", fromDate).queryParam("to", toDate))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(0)));
		//.andExpect(jsonPath("$[0].from", is(fromDate)))
		//.andExpect(jsonPath("$[0].to", is(toDate)));

		// modify reservation
		RequestedReservation patch = new RequestedReservation();
		patch.setDates(newDates);
		
		// verify dates have changed
		mvc.perform(patch("/camping/v1.0/reservations/" + reservationId)
				.content(objectMapper.writeValueAsString(patch))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.dates.arrival", is(newFromDate)))
		.andExpect(jsonPath("$.dates.departure", is(newDepartureDate)));
		

		// verify old reserved dates are available again
		mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", fromDate).queryParam("to", toDate))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)));
		
		// verify new reserved dates are not available
		mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", newFromDate).queryParam("to", newToDate))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(0)));
	}
}
