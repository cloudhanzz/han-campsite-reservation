package han.jiayun.campsite.reservation.stress.tests;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import han.jiayun.campsite.reservation.controllers.ControllerTestParent;
import han.jiayun.campsite.reservation.model.FromTo;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test resporting available date ranges")
public class ConcurrentAvailabilityInquiryTest extends ControllerTestParent {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("Test concurrently respond to 100,000 availability inquiries")
	@Test
	void concurrent_respond_to_100_000_availability_inquiries() throws Exception {
		
		System.out.println("\nTest concurrently respond to 100,000 availability inquiries, please wait ...");
		
		Random random = new Random();

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		List<Future<List<FromTo>>> resultList = new ArrayList<>();

		LocalDate now = LocalDate.now();
		
		for (int i = 0; i < 100_000; i++) {

			int startPlus = random.nextInt(20) + 1;
			int fromStart = random.nextInt(25);
			int endPlus = Integer.min(30, fromStart);
			
			LocalDate from = now.plusDays(startPlus);
			LocalDate to = from.plusDays(endPlus);

			String fromDate = from.format(DATETIME_FORMATTER);
			String toDate = to.format(DATETIME_FORMATTER);
			AvailabilityAsker asker = new AvailabilityAsker(mvc, objectMapper, fromDate, toDate);

			Future<List<FromTo>> r = executor.submit(asker);
			resultList.add(r);
		}

		do {
			TimeUnit.MILLISECONDS.sleep(100);
		} while (executor.getCompletedTaskCount() < resultList.size());

		System.out.printf("Inquiry Results\n");
		for (int i = 0; i < resultList.size(); i++) {
			Future<List<FromTo>> result = resultList.get(i);
			String availability = objectMapper.writeValueAsString(result.get());
			System.out.printf("Inquiry %d: %s\n", i+1, availability);
		}

		executor.shutdown();
	}

}
