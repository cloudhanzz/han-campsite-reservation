package han.jiayun.campsite.reservation.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * The reservation confirmed by the system
 * 
 * @author Jiayun Han
 *
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ConfirmedReservation extends RequestedReservation {

	private String id;
	
	private UserInfo user;	
	private Schedule dates;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime reservedAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime modifiedAt;	
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime cancelledAt;
	
	@JsonIgnore
	public boolean isActive() {
		return cancelledAt == null;
	}

	public ConfirmedReservation() {
		this.id = UUID.randomUUID().toString();
	}
}
