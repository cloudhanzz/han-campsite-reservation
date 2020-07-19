package han.jiayun.campsite.reservation.model;

import han.jiayun.campsite.reservation.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User information that is required when requesting a reservation
 *
 * @author Jiayun Han
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

	@Required
	private String firstName;
	
	@Required
	private String lastName;
	
	@Required
	private String email;
}
