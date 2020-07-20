package han.jiayun.campsite.reservation.validators;

import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;

import han.jiayun.campsite.reservation.exceptions.InvalidRequestBodyException;
import han.jiayun.campsite.reservation.model.Schedule;
import han.jiayun.campsite.reservation.model.UserInfo;
import han.jiayun.campsite.reservation.service.RequestValidator;

/**
 * Checking the user of the request contains valid values. This happens after
 * the missing value checking
 * <p>
 * 
 * @author Jiayun Hans
 *
 */
public final class UserInfoChecker implements RequestValidator {
	
	private final EmailValidator emailValidator = EmailValidator.getInstance();
	private final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]+");

	@Override
	public void check(UserInfo user, Schedule dates) {

		checkName(user.getFirstName(), "First Name");
		checkName(user.getLastName(), "Last Name");		
		checkEmail(user.getEmail());
	}

	/**
	 * For simplicity, it only checks that starts with a letter.
	 * @param name
	 * @param header for messaging purpose
	 */
	public void checkName(String name, String header) {
		if(!NAME_PATTERN.matcher(name).matches()) {
			throw new InvalidRequestBodyException("Invalid " + header);
		}		
	}

	/**
	 * It relies on Apache Email validator
	 * @param email
	 */
	public void checkEmail(String email) {
		if(!emailValidator.isValid(email)) {
			throw new InvalidRequestBodyException("valid Email");
		}		
	}
}
