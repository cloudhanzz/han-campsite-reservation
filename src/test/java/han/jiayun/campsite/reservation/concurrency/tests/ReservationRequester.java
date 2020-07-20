package han.jiayun.campsite.reservation.concurrency.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.concurrent.Callable;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import han.jiayun.campsite.reservation.model.CreationResponse;

public class ReservationRequester implements Callable<Boolean> {

	private ObjectMapper objectMapper;
	private MockMvc mvc;
	private String request;

	public ReservationRequester(MockMvc mvc, ObjectMapper objectMapper, String request) {
		this.mvc = mvc;
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	public Boolean call() throws Exception {

		// Create reservation
		String result = mvc
				.perform(post("/camping/v1.0/reservations").content(request)
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andReturn().getResponse().getContentAsString();

		CreationResponse response = objectMapper.readValue(result, CreationResponse.class);
		return response.getReservationId() != null;
	}

}
