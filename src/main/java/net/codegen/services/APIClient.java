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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class APIClient
{
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;

	private static final Logger log = LoggerFactory.getLogger( Scheduler.class );
	private static final String WORD_PARAMETERS = "parameters";

	public List<EventEventBriteAPI> getEventsListFromEventBriteAPI( int cityIndex )
	{
		//cityIndex is 0 if Melbourne and 1 if Brisbane
		ResponseEventBriteAPI.setCityIndex( cityIndex );
		String cityName = cityIndex == 0 ? "melbourne" : "brisbane";
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
			System.out.println(nextEventsList.get( 0 ).getName());
			events.addAll( nextEventsList );
		}
		return events;
	}

	public List<EventEventFulAPI> getEventsListFromEventFulAPI( int cityIndex )
	{
		//cityIndex is 0 if Melbourne and 1 if Brisbane
		ResponseEventFulAPI.setCityIndex( cityIndex );
		String cityName = cityIndex == 0 ? "melbourne" : "brisbane";
		log.info( "Getting events from EventBriteAPI for city {}",cityName );
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
		return events;
	}
}
