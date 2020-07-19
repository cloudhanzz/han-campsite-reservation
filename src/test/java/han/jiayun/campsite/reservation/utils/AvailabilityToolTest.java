package han.jiayun.campsite.reservation.utils;

import static han.jiayun.campsite.reservation.util.AvailabilityTool.isDateAvailable;
import static han.jiayun.campsite.reservation.util.AvailabilityTool.isDateNotAvailable;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.DesiredDates;

@DisplayName("Test the Availability Tool")
public class AvailabilityToolTest {

	private Map<String, ConfirmedReservation> reservations;

	@BeforeEach
	void setup() {
		reservations = new ConcurrentHashMap<>();
	}

	@Test
	@DisplayName("Test date is available")
	public void testDateAvailable() {

		LocalDate now = LocalDate.now();
		LocalDate date;
		for (int i = 1; i < 31; i++) {
			date = now.plusDays(i);
			assertTrue(isDateAvailable(reservations, date));
		}
	}

	@Test
	@DisplayName("Test dates are available while others not")
	public void testDateNotAvailable() {
		
		LocalDate now = LocalDate.now();
		LocalDate day3 = now.plusDays(3);
		LocalDate day4 = now.plusDays(4);
		LocalDate day5 = now.plusDays(5);
		LocalDate day6 = now.plusDays(6);
		
		
		ConfirmedReservation r = new ConfirmedReservation();
		DesiredDates dates = new DesiredDates(day3, day6);
		r.setDates(dates);
		r.setReservedAt(LocalDateTime.now());
		
		reservations.put(r.getId(), r);
		assertTrue(isDateNotAvailable(reservations, day3));
		assertTrue(isDateNotAvailable(reservations, day4));
		assertTrue(isDateNotAvailable(reservations, day5));
		assertTrue(isDateAvailable(reservations, day6));
	}
}
