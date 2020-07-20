package han.jiayun.campsite.reservation.stress.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import han.jiayun.campsite.reservation.model.FromTo;

public class AvailabilityAsker implements Callable<List<FromTo>> {
	
	private ObjectMapper objectMapper;
	private MockMvc mvc;
	private String fromDate;
	private String toDate;
	
	public AvailabilityAsker (MockMvc mvc, ObjectMapper objectMapper, String fromDate, String toDate) {
		this.mvc = mvc;
		this.objectMapper = objectMapper;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	@Override
	public List<FromTo> call() throws Exception {
		
		String result = mvc.perform(get("/camping/v1.0/reservations/available/dates")
				.queryParam("from", fromDate).queryParam("to", toDate))
				.andReturn()
				.getResponse()
				.getContentAsString();
		
		CollectionType javaType = objectMapper.getTypeFactory()
			      .constructCollectionType(List.class, FromTo.class);
		
		List<FromTo> fromTo = objectMapper.readValue(result, javaType);
		
		return fromTo;
	}

}
