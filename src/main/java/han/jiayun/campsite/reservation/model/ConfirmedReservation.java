package han.jiayun.campsite.reservation.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import han.jiayun.campsite.reservation.annotation.NotCopiable;
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

	@NotCopiable
	private String id;
	
	private UserInfo user;	
	private Schedule dates;

	@NotCopiable
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime reservedAt;
	
	@NotCopiable
	private LocalDateTime modifiedAt;
	
	@NotCopiable
	private LocalDateTime cancelledAt;
	
	@JsonIgnore
	public boolean isActive() {
		return cancelledAt == null;
	}

	public ConfirmedReservation() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}
}
