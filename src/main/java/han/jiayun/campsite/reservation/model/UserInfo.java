package han.jiayun.campsite.reservation.model;

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

	private final String firstName;
	private final String lastName;
	private String email;
}
