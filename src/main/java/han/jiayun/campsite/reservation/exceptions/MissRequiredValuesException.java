package han.jiayun.campsite.reservation.exceptions;

import static java.util.stream.Collectors.joining;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * An exception of this class is triggered if the request body or its children
 * miss one or more required attributes.
 * 
 * @author Jiayun Han
 *
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MissRequiredValuesException extends RestException {

	private static final String NOTE = "Please ensure provide the required information";

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param missingParts
	 *            The names of the missing attributes
	 */
	public MissRequiredValuesException(List<String> missingParts) {
		super("missing " + missingParts.stream().collect(joining(", ")), HttpStatus.UNPROCESSABLE_ENTITY, NOTE);
	}
}
