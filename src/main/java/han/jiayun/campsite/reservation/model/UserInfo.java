package han.jiayun.campsite.reservation.model;

import han.jiayun.campsite.reservation.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * User information that is required when requesting a reservation
 *
 * @author Jiayun Han
 */
@Getter
@Setter
@AllArgsConstructor
public class UserInfo {

	@Required
	private final String firstName;
	
	@Required
	private final String lastName;
	
	@Required
	private String email;
}
