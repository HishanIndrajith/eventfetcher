package net.codegen.shedulars;

import net.codegen.models.Event;
import net.codegen.models.event_brite_api.EventEventBriteAPI;
import net.codegen.models.event_brite_api.ResponseEventBriteAPI;
import net.codegen.models.event_ful_api.EventEventFulAPI;
import net.codegen.models.event_ful_api.ResponseEventFulAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import net.codegen.services.EventService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SheduledEventAPIClient
{
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private EventService eventService;

	//Full event List for a single save operation
	private static List<Event> eventList=null;

	// static method to create instance of Singleton class
	//Singleton pattern used for eventlist as same object can be used again and again.
	private static List<Event> getEventList()
	{
		if ( eventList == null )
			eventList = new ArrayList<>();
		return eventList;
	}

	private static final Logger log = LoggerFactory.getLogger( SheduledEventAPIClient.class );
	private static final String PARAMETERS = "parameters";
	static
	{
		//implement the array list
		eventList = getEventList();
	}

	/*
	https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
	The pattern is a list of six single space-separated fields: representing second, minute, hour, day, month, weekday
	Cron Sequences used
	"0 * * * * *" - 0th second of each minute
	"0 0 10 * * *" - 10th hour of each day
	"0 0 10 1 * *" - 10th hour of first day of each month
	*/
	@Scheduled(cron = "* * * * * *")
	public void reportCurrentTime()
	{
		log.info( "starting" );
		List<EventEventBriteAPI> eventListFromEventBriteAPIForMelbourne = getEventsListFromEventBriteAPI( 0 );
		List<EventEventBriteAPI> eventListFromEventBriteAPIForBrisbane = getEventsListFromEventBriteAPI( 1 );
		List<EventEventFulAPI> eventsListFromEventFulAPIAPIForMelbourne = getEventsListFromEventFulAPI( 0 );
		List<EventEventFulAPI> eventsListFromEventFulAPIAPIForBrisbane = getEventsListFromEventFulAPI( 1 );
		eventList.addAll( eventListFromEventBriteAPIForMelbourne );
		eventList.addAll( eventListFromEventBriteAPIForBrisbane );
		eventList.addAll( eventsListFromEventFulAPIAPIForMelbourne );
		eventList.addAll( eventsListFromEventFulAPIAPIForBrisbane );
		eventService.insertEvent( eventList );
		log.info( "Successfully updated the database" );
	}

	private List<EventEventBriteAPI> getEventsListFromEventBriteAPI( int cityIndex )
	{
		log.info( "API= EventBriteAPI" );
		//cityIndex is 0 if Melbourne and 1 if Brisbane
		ResponseEventBriteAPI.setCityIndex( cityIndex );
		String cityName = cityIndex == 0 ? "melbourne" : "brisbane";
		String authToken = "OYPUWKNJCVLUGYVQCPKH";

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON ) );
		headers.add( "User-Agent", "Spring's RestTemplate" );  // value can be whatever
		headers.add( "Authorization", "Bearer " + authToken );

		ResponseEntity<ResponseEventBriteAPI> initialResponseEntity = restTemplate.exchange(
				"https://www.eventbriteapi.com/v3/events/search?location.address=" + cityName + "&expand=venue&page=1",
				HttpMethod.GET,
				new HttpEntity<>( PARAMETERS, headers ),
				ResponseEventBriteAPI.class
		);
		ResponseEventBriteAPI initialResponse = initialResponseEntity.getBody();
		int pageCount = initialResponse.getPagination().getPageCount();
		List<EventEventBriteAPI> events = initialResponse.getEvents();
		for ( int i = 1; i < pageCount; i++ )
		{
			ResponseEntity<ResponseEventBriteAPI> responseEntity = restTemplate.exchange(
					"https://www.eventbriteapi.com/v3/events/search?location.address=" + cityName
							+ "&expand=venue&page=" + ( i + 1 ),
					HttpMethod.GET,
					new HttpEntity<>( PARAMETERS, headers ),
					ResponseEventBriteAPI.class
			);
			ResponseEventBriteAPI response = responseEntity.getBody();
			List<EventEventBriteAPI> nextEventsList = response.getEvents();
			events.addAll( nextEventsList );
		}
		return events;
	}

	private List<EventEventFulAPI> getEventsListFromEventFulAPI( int cityIndex )
	{
		//cityIndex is 0 if Melbourne and 1 if Brisbane
		ResponseEventFulAPI.setCityIndex( cityIndex );
		String cityName = cityIndex == 0 ? "melbourne" : "brisbane";

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept( Collections.singletonList( MediaType.APPLICATION_XML ) );
		HttpEntity<String> entity = new HttpEntity<>( PARAMETERS, headers );

		ResponseEntity<ResponseEventFulAPI> initialResponseEntity = restTemplate.exchange(
				"http://api.eventful.com/rest/events/search?app_key=pzZR48qPz4pSzNQN&location=" + cityName
						+ "&sort_order=date&page_number=5", HttpMethod.GET, entity, ResponseEventFulAPI.class );
		ResponseEventFulAPI initialResponse = initialResponseEntity.getBody();
		int pageCount = Integer.parseInt( initialResponse.getPageCount() );
		List<EventEventFulAPI> events = initialResponse.getEvents();
		for ( int i = 1; i < pageCount; i++ )
		{
			ResponseEntity<ResponseEventFulAPI> responseEntity = restTemplate.exchange(
					"http://api.eventful.com/rest/events/search?app_key=pzZR48qPz4pSzNQN&location=" + cityName
							+ "&sort_order=date&page_number=" + ( i + 1 ),
					HttpMethod.GET,
					new HttpEntity<>( PARAMETERS, headers ),
					ResponseEventFulAPI.class
			);
			ResponseEventFulAPI response = responseEntity.getBody();
			List<EventEventFulAPI> nextEventsList = response.getEvents();
			events.addAll( nextEventsList );
		}
		return events;
	}
}
