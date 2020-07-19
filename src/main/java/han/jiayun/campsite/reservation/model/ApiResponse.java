package han.jiayun.campsite.reservation.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String reservationId;
	private String status;
	private String message;
}
