package han.jiayun.campsite.reservation.concurrency.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import han.jiayun.campsite.reservation.controllers.ControllerTestParent;
import han.jiayun.campsite.reservation.model.RequestedReservation;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;
import han.jiayun.campsite.reservation.repositories.ReservationRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test resporting available date ranges")
public class ConcurrentReservationRequestsTest extends ControllerTestParent {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	public void cancelExistingReservations() throws Exception {
		Set<String> ids = ReservationRepository.INSTANCE.getReservationIds();
		for (String id : ids) {
			mvc.perform(delete("/camping/v1.0/reservations/" + id));
		}	
	}

	@DisplayName("Test concurrently respond to 1000 reservation requests of the same periods expecting only one is successful")
	@Test
	void concurrent_respond_to_1000_reservation_requests_and_only_one_should_succeed() throws Exception {

		System.out.println(
				"\nTest concurrently respond to 100 reservation requests of the same periods expecting only one is successful, please wait ...");

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		List<Future<Boolean>> resultList = new ArrayList<>();

		LocalDate today = LocalDate.now();
		LocalDate arrival = today.plusDays(1);
		LocalDate departure = arrival.plusDays(2);
		Schedule dates = new Schedule(arrival, departure);

		UserInfo USER = new UserInfo("Jiayun", "Han", "jiayunhan@gmail.com");
		RequestedReservation requestedRequest = new RequestedReservation();

		requestedRequest.setUser(USER);
		requestedRequest.setDates(dates);

		String request = objectMapper.writeValueAsString(requestedRequest);

		for (int i = 0; i < 1000; i++) {

			ReservationRequester requester = new ReservationRequester(mvc, objectMapper, request);
			Future<Boolean> r = executor.submit(requester);
			resultList.add(r);
		}

		do {
			TimeUnit.MILLISECONDS.sleep(50);
		} while (executor.getCompletedTaskCount() < resultList.size());

		int reserved = 0;

		for (int i = 0; i < resultList.size(); i++) {
			Future<Boolean> result = resultList.get(i);
			boolean withId = result.get();
			if (withId) {
				reserved++;
			}
		}

		executor.shutdown();

		assertEquals(1, reserved, "Only one reservation can be made for the same dates");
	}

}
