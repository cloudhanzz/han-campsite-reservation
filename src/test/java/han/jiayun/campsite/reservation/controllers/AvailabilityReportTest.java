package han.jiayun.campsite.reservation.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import han.jiayun.campsite.reservation.repositories.ReservationRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test resporting available date ranges")
public class AvailabilityReportTest extends ControllerTestParent{

	@Autowired
	private MockMvc mvc;
		
	@BeforeEach
	public void cancelExistingReservations() throws Exception {
		Set<String> ids = ReservationRepository.INSTANCE.getReservationIds();
		for(String id : ids) {
			mvc.perform(delete("/camping/v1.0/reservations/" + id));
		}
	}
	
	@DisplayName("Test get available dates when no date range is provided")
	@Test
	void testGetAvailableDatesDefaultOneMonth() throws Exception {
		
		LocalDate now = LocalDate.now();
		LocalDate from = now.plusDays(1);
		LocalDate to = now.plusMonths(1);
		
		String fromDate = from.format(DATETIME_FORMATTER);
		String toDate = to.format(DATETIME_FORMATTER);
		
		mvc.perform(get("/camping/v1.0/reservations/available/dates"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].from", is(fromDate)))
		.andExpect(jsonPath("$[0].to", is(toDate)));
	}
	
	@DisplayName("Test get available dates of the given starting date")
	@Test
	void testGetAvailableDatesWithStartingDate() throws Exception {
		
		LocalDate now = LocalDate.now();
		LocalDate from = now.plusDays(10);
		LocalDate to = now.plusMonths(1);
		
		String fromDate = from.format(DATETIME_FORMATTER);
		String toDate = to.format(DATETIME_FORMATTER);
		
		mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", fromDate))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].from", is(fromDate)))
		.andExpect(jsonPath("$[0].to", is(toDate)));
	}
	
	@DisplayName("Test get available dates of the given ending date")
	@Test
	void testGetAvailableDatesWithEndingDate() throws Exception {
		
		LocalDate now = LocalDate.now();
		LocalDate from = now.plusDays(1);
		LocalDate to = now.plusMonths(1);
		
		String fromDate = from.format(DATETIME_FORMATTER);
		String toDate = to.format(DATETIME_FORMATTER);
		
		mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("to", toDate))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].from", is(fromDate)))
		.andExpect(jsonPath("$[0].to", is(toDate)));
	}
	
	
	@DisplayName("Test get available dates of the given starting and ending dates")
	@Test
	void testGetAvailableDatesWithStartingAndEndingDates() throws Exception {
		
		LocalDate now = LocalDate.now();
		LocalDate from = now.plusDays(10);
		LocalDate to = now.plusMonths(1);
		
		String fromDate = from.format(DATETIME_FORMATTER);
		String toDate = to.format(DATETIME_FORMATTER);
		
		mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", fromDate).queryParam("to", toDate))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].from", is(fromDate)))
		.andExpect(jsonPath("$[0].to", is(toDate)));
	}	
	
	@DisplayName("Test get starting date is same as ending date")
	@Test
	void testStartingDateSameAsEndingDate() throws Exception {
		
		LocalDate now = LocalDate.now();
		String date = now.plusDays(10).format(DATETIME_FORMATTER);
		
		mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", date).queryParam("to", date))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].from", is(date)))
		.andExpect(jsonPath("$[0].to", is(date)));
	}
	
	@DisplayName("Test get starting date is after ending date")
	@Test
	void testStartingDateAfterEndingDate() throws Exception {
		
		LocalDate now = LocalDate.now();
		LocalDate from = now.plusDays(10);
		LocalDate to = from.minusDays(1);
		
		String fromDate = from.format(DATETIME_FORMATTER);
		String toDate = to.format(DATETIME_FORMATTER);
		
		mvc.perform(get("/camping/v1.0/reservations/available/dates").queryParam("from", fromDate).queryParam("to", toDate))
		.andExpect(status().is4xxClientError());
	}

}
