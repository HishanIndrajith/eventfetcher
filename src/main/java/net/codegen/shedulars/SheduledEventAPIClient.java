package net.codegen.shedulars;

import net.codegen.models.Event;
import net.codegen.models.ResponseEventBriteAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import net.codegen.services.EventService;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class SheduledEventAPIClient
{
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EventService eventService;

	private static final Logger log = LoggerFactory.getLogger(SheduledEventAPIClient.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/*
	https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
	The pattern is a list of six single space-separated fields: representing second, minute, hour, day, month, weekday
	Cron Sequences used
	"0 * * * * *" - 0th second of each minute
	"0 0 10 * * *" - 10th hour of each day
	"0 0 10 1 * *" - 10th hour of first day of each month
	*/
	@Scheduled(cron = "0 * * * * *")
	public void reportCurrentTime() {
		log.info("The time is now {}", dateFormat.format(new Date()));


		String authToken = "OYPUWKNJCVLUGYVQCPKH";

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON));
		headers.add("User-Agent", "Spring's RestTemplate" );  // value can be whatever
		headers.add("Authorization", "Bearer "+authToken );


		ResponseEntity<ResponseEventBriteAPI> initialResponseEntity = restTemplate.exchange(
				"https://www.eventbriteapi.com/v3/events/search?location.address=melbourne&expand=venue&page=1",
				HttpMethod.GET,
				new HttpEntity<>("parameters", headers),
				ResponseEventBriteAPI.class
		);
		ResponseEventBriteAPI initialResponse = initialResponseEntity.getBody();
		int pageCount = initialResponse.getPagination().getPageCount();
		System.out.println("page count "+pageCount);
		List<Event> events = initialResponse.getEvents();
		eventService.insertEvent(events);
		for(int i=1;i<pageCount;i++){
			System.out.println("sending for page "+i);
			ResponseEntity<ResponseEventBriteAPI> responseEntity = restTemplate.exchange(
					"https://www.eventbriteapi.com/v3/events/search?location.address=melbourne&expand=venue&page="+(i+1),
					HttpMethod.GET,
					new HttpEntity<>("parameters", headers),
					ResponseEventBriteAPI.class
			);
			ResponseEventBriteAPI response = responseEntity.getBody();
			eventService.insertEvent( response.getEvents() );
		}

		log.info("Successfully updated the database");
	}
}
