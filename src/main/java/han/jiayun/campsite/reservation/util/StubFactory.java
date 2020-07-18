package han.jiayun.campsite.reservation.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import han.jiayun.campsite.reservation.model.ConfirmedReservation;
import han.jiayun.campsite.reservation.model.DesiredDates;
import han.jiayun.campsite.reservation.model.FromTo;
import han.jiayun.campsite.reservation.model.UserInfo;

/**
 * A factory to produce stub for quick-test of controller skeleton
 * @author Jiayun Han
 *
 */
public final class StubFactory {
	
	private StubFactory() {}
	
	public static final ConfirmedReservation ORIGINAL_RESERVATION;
	public static final ConfirmedReservation MODIFIED_RESERVATION;
	public static final List<FromTo> OPEN_DATES;
	
	static {
		
		LocalDateTime reservedAt = LocalDateTime.now();
		LocalDateTime modifiedAt = reservedAt.plusMinutes(1800); // 30 hours later
		
		LocalDate now = LocalDate.now();
		LocalDate arrival = now.plusDays(10);
		LocalDate departure = arrival.plusDays(2);
		
		UserInfo user = new UserInfo("John", "Doe", "john.doe@gmail.com");
		
		DesiredDates originalDates = new DesiredDates(arrival, departure);
		DesiredDates newDates = new DesiredDates(arrival.plusDays(5), departure.plusDays(5));
		
		ORIGINAL_RESERVATION = new ConfirmedReservation();		
		ORIGINAL_RESERVATION.setDates(originalDates);
		ORIGINAL_RESERVATION.setUser(user);
		ORIGINAL_RESERVATION.setReservedAt(reservedAt);
		
		MODIFIED_RESERVATION = new ConfirmedReservation();
		MODIFIED_RESERVATION.setDates(newDates);
		MODIFIED_RESERVATION.setUser(user);
		MODIFIED_RESERVATION.setReservedAt(reservedAt);
		MODIFIED_RESERVATION.setModifiedAt(modifiedAt);
		
		FromTo ft = new FromTo();
		ft.setFrom(now.plusDays(20));
		ft.setTo(now.plusDays(25));
		
		FromTo ft2 = new FromTo();
		ft2.setFrom(now.plusDays(28));
		ft2.setTo(now.plusDays(30));
		
		OPEN_DATES = Arrays.asList(ft, ft2);
	}

}
