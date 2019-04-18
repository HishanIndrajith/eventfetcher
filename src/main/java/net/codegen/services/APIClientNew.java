package net.codegen.services;

import net.codegen.Scheduler;
import net.codegen.models.event_brite_api.EventEventBriteAPI;
import net.codegen.models.event_brite_api.ResponseEventBriteAPI;
import net.codegen.models.event_ful_api.EventEventFulAPI;
import net.codegen.models.event_ful_api.ResponseEventFulAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class APIClientNew
{
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;

	private static final Logger log = LoggerFactory.getLogger( Scheduler.class );
	private static final String WORD_PARAMETERS = "parameters";

	@Async
	public CompletableFuture<List<EventEventBriteAPI>> getEventsListFromEventBriteAPI( String cityName)
	{
		log.info( "Getting events from EventBriteAPI for city {}",cityName );
		String authToken = environment.getProperty( "eventapi.eventbrite.token" );

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON ) );
		headers.add( "User-Agent", "Spring's RestTemplate" );  // value can be whatever
		headers.add( "Authorization", "Bearer " + authToken );

		ResponseEntity<ResponseEventBriteAPI> initialResponseEntity = restTemplate.exchange(
				"https://www.eventbriteapi.com/v3/events/search?location.address=" + cityName + "&expand=venue&page=1",
				HttpMethod.GET,
				new HttpEntity<>( WORD_PARAMETERS, headers ),
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
					new HttpEntity<>( WORD_PARAMETERS, headers ),
					ResponseEventBriteAPI.class
			);
			ResponseEventBriteAPI response = responseEntity.getBody();
			List<EventEventBriteAPI> nextEventsList = response.getEvents();
			events.addAll( nextEventsList );
		}
		events.replaceAll(event -> {
			event.setCity(cityName);
			return event;
		});
		Scheduler.count++;
		System.out.println("Progress : "+ Scheduler.count + " / 61" );
		return CompletableFuture.completedFuture(events);
	}

	@Async
	public CompletableFuture<List<EventEventFulAPI>> getEventsListFromEventFulAPI( String cityName )
	{
		log.info( "Getting events from EventFulAPI for city {}",cityName );
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept( Collections.singletonList( MediaType.APPLICATION_XML ) );
		HttpEntity<String> entity = new HttpEntity<>( WORD_PARAMETERS, headers );
		String authToken = environment.getProperty( "eventapi.eventful.token" );

		ResponseEntity<ResponseEventFulAPI> initialResponseEntity = restTemplate.exchange(
				"http://api.eventful.com/rest/events/search?app_key="+authToken+"&location=" + cityName
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
					new HttpEntity<>( WORD_PARAMETERS, headers ),
					ResponseEventFulAPI.class
			);
			ResponseEventFulAPI response = responseEntity.getBody();
			List<EventEventFulAPI> nextEventsList = response.getEvents();
			events.addAll( nextEventsList );
		}
		events.replaceAll(event -> {
			event.setCity(cityName);
			return event;
		});
		return CompletableFuture.completedFuture(events);
	}
}
