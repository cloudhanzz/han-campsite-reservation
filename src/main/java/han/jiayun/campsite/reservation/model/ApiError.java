package han.jiayun.campsite.reservation.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
	private HttpStatus status;
	private String message;
	private List<String> notes;

	public ApiError(HttpStatus status, String message, String note) {
		this(status, message, Arrays.asList(note));
	}
}
