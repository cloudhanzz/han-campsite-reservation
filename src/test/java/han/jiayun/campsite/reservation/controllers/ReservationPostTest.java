package han.jiayun.campsite.reservation.controllers;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import han.jiayun.campsite.reservation.exceptions.InvalidRequestBodyException;
import han.jiayun.campsite.reservation.exceptions.InvalidReservationLengthException;
import han.jiayun.campsite.reservation.exceptions.MissRequiredValuesException;
import han.jiayun.campsite.reservation.exceptions.ReservationTooSoonOrTooFarAwayException;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;
import han.jiayun.campsite.reservation.repositories.ReservationRepository;
import han.jiayun.campsite.reservation.validators.ArrivalDateChecker;
import han.jiayun.campsite.reservation.validators.ReservationLengthChecker;

/**
 * 
 * @author Jiayun Han
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test creating reservations")
public class ReservationPostTest {
	
	private static final UserInfo USER = new UserInfo("Jiayun", "Han", "jiayunhan@gmail.com");

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
	
	@ParameterizedTest
	@CsvFileSource(resources = "/insufficient_request_information.csv")
	@DisplayName("reservation cannot be made with missing info")
	public void missing_user_info_captured(String error, String request) throws JsonProcessingException, Exception {		

		mvc.perform(post("/camping/v1.0/reservations").content(request)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof MissRequiredValuesException))
				.andExpect(result -> assertEquals(error, result.getResolvedException().getMessage()));
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/invalid_user_information.csv")
	@DisplayName("reservation cannot be made with invalid user info info")
	public void invalid_user_info_captured(String error, String request) throws JsonProcessingException, Exception {		

		mvc.perform(post("/camping/v1.0/reservations").content(request)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidRequestBodyException))
				.andExpect(result -> assertEquals(error, result.getResolvedException().getMessage()));
	}
	
	@Test
	@DisplayName("The campsite can be reserved minimum 1 day(s) ahead of arrival")
	public void reservation_minimum_1_day_in_advance() throws Exception {
		LocalDate today = LocalDate.now();
		LocalDate departure = today.plusDays(3);
		Schedule dates = new Schedule(today, departure);
		
		RequestedReservation request = new RequestedReservation(USER, dates);
		mvc.perform(post("/camping/v1.0/reservations")
				     .content(objectMapper.writeValueAsString(request))
				     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof ReservationTooSoonOrTooFarAwayException))
		.andExpect(result -> assertEquals(ArrivalDateChecker.ERROR_LESS_THAN_MIN_DAYS_IN_ADVANCE, result.getResolvedException().getMessage()));
	}
	
	@Test
	@DisplayName("The campsite can be reserved maximum 1 month (30 days) in advance")
	public void reservation_maximum_30_day_in_advance() throws Exception {
		LocalDate today = LocalDate.now();
		LocalDate arrival = today.plusDays(31);
		LocalDate departure = arrival.plusDays(3);
		Schedule dates = new Schedule(arrival, departure);
		
		RequestedReservation request = new RequestedReservation(USER, dates);
		mvc.perform(post("/camping/v1.0/reservations")
				     .content(objectMapper.writeValueAsString(request))
				     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof ReservationTooSoonOrTooFarAwayException))
		.andExpect(result -> assertEquals(ArrivalDateChecker.ERROR_MORE_THAN_MAX_DAYS_IN_ADVANCE, result.getResolvedException().getMessage()));
	}
	
	@Test
	@DisplayName("The campsite cannot be reserved for less than one day")
	public void reservation_cannot_be_reserved_for_less_than_one_day() throws Exception {
		LocalDate today = LocalDate.now();
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = today.plusDays(1);
		Schedule dates = new Schedule(arrival, departure);
		
		RequestedReservation request = new RequestedReservation(USER, dates);
		mvc.perform(post("/camping/v1.0/reservations")
				     .content(objectMapper.writeValueAsString(request))
				     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidReservationLengthException))
		.andExpect(result -> assertEquals(ReservationLengthChecker.ERROR_DEPARTURE_NOT_AFTER_ARRIVAL, result.getResolvedException().getMessage()));
	}
	
	@Test
	@DisplayName("The campsite cannot be reserved for more than 3 days")
	public void reservation_cannot_be_reserved_for_more_than_3_days() throws Exception {
		LocalDate today = LocalDate.now();
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = arrival.plusDays(4);
		Schedule dates = new Schedule(arrival, departure);
		
		RequestedReservation request = new RequestedReservation(USER, dates);
		mvc.perform(post("/camping/v1.0/reservations")
				     .content(objectMapper.writeValueAsString(request))
				     .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidReservationLengthException))
		.andExpect(result -> assertEquals(ReservationLengthChecker.ERROR_BEYOND_MAX_RESERVATION_DAYS, result.getResolvedException().getMessage()));
	}

	@Test
	@DisplayName("😱: Test Successfully Created Reservation")
	public void created_reservation_OK() throws Exception {

		LocalDate today = LocalDate.now();
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = arrival.plusDays(2);
		Schedule dates = new Schedule(arrival, departure);
		RequestedReservation request = new RequestedReservation();

		request.setUser(USER);
		request.setDates(dates);

		mvc.perform(post("/camping/v1.0/reservations").content(objectMapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$").value(hasKey("reservationId")));
	}

}
