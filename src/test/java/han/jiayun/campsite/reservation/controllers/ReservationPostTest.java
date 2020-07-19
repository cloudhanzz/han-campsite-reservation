package han.jiayun.campsite.reservation.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import han.jiayun.campsite.reservation.exceptions.MissRequiredValuesException;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test creating reservations")
public class ReservationPostTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("Test missing required info")
	public void no_user_error() throws JsonProcessingException, Exception {

		List<RequestedReservation> requests = new ArrayList<>();
		requests.add(new RequestedReservation());

		UserInfo user = new UserInfo(null, null, null);
		Schedule dates = new Schedule(null, null);
		RequestedReservation r2 = new RequestedReservation();
		r2.setUser(user);
		r2.setDates(dates);
		requests.add(r2);

		for (RequestedReservation request : requests) {

			mvc.perform(post("/camping/v1.0/reservations").content(objectMapper.writeValueAsString(request))
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
					.andExpect(status().is4xxClientError()).andExpect(
							result -> assertTrue(result.getResolvedException() instanceof MissRequiredValuesException));

		}
	}

	@Test
	@DisplayName("Test Created Reservation")
	public void created() throws Exception {

		UserInfo user = new UserInfo("John", "Doe", "john.doe@gmail.com");

		LocalDate arrival = LocalDate.of(2020, 8, 2);
		LocalDate departure = LocalDate.of(2020, 8, 5);
		Schedule dates = new Schedule(arrival, departure);
		RequestedReservation request = new RequestedReservation();

		request.setUser(user);
		request.setDates(dates);

		mvc.perform(post("/camping/v1.0/reservations").content(objectMapper.writeValueAsString(request))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}

}
