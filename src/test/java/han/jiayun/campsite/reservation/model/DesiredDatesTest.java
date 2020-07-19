package han.jiayun.campsite.reservation.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test DesiredDate model")
public class DesiredDatesTest {
	
	@Test
	@DisplayName("Test converting from arrival and departure dates to actual reserved dates")
	public void testActualDates() {
		
		LocalDate now = LocalDate.now();
		LocalDate arrival = now.plusDays(3);
		LocalDate day4 = now.plusDays(4);
		LocalDate day5 = now.plusDays(5);
		LocalDate departure = arrival.plusDays(3);
		DesiredDates arrivalDeparture = new DesiredDates(arrival, departure);
		List<LocalDate> dates = arrivalDeparture.toDiscreteDates();
		
		assertAll( "dates should contain from arrival date until (excluding) departure date",
				() -> assertEquals(3, dates.size()),
				() -> assertTrue(dates.contains(arrival)),
				() -> assertTrue(dates.contains(day4)),
				() -> assertTrue(dates.contains(day5)),
				() -> assertFalse(dates.contains(departure))
				);		
	}

}
