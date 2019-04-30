package net.codegen.services;

import net.codegen.Scheduler;
import net.codegen.models.event_brite_api.CategoryEventBrite;
import net.codegen.models.event_brite_api.CategoryResponseEventBrite;
import net.codegen.models.event_brite_api.EventEventBriteAPI;
import net.codegen.models.event_brite_api.ResponseEventBriteAPI;
import net.codegen.models.event_ful_api.CategoryEventFul;
import net.codegen.models.event_ful_api.CategoryResponseEventFul;
import net.codegen.models.event_ful_api.EventEventFulAPI;
import net.codegen.models.event_ful_api.ResponseEventFulAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
	public CompletableFuture<List<EventEventBriteAPI>> getEventsListFromEventBriteAPI( String cityName,List<CategoryEventBrite> categories)
	{
		log.info( "Getting events from EventBriteAPI for city {}", cityName );
		//List of events for city
		List<EventEventBriteAPI> eventsForGivenCity = new ArrayList<>(  );
		// Get events for the given city for each category and get a single list finally
		for(CategoryEventBrite category : categories)
		{
			//log.info( "Getting events from EventBriteAPI for city {}", cityName );
			String authToken = environment.getProperty( "eventapi.eventbrite.token" );

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON ) );
			headers.add( "User-Agent", "Spring's RestTemplate" );  // value can be whatever
			headers.add( "Authorization", "Bearer " + authToken );

			ResponseEntity<ResponseEventBriteAPI> initialResponseEntity = restTemplate.exchange(
					"https://www.eventbriteapi.com/v3/events/search?categories=" + category.getId()
							+ "&location.address=" + cityName + "&expand=venue&page=1",
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
			// Add the city name and the category name to the events
			events.replaceAll( event -> {
				event.setCity( cityName );
				event.setCategory( category.getName() );
				return event;
			} );
			// Add the events for the category to the main list
			eventsForGivenCity.addAll(events);
		}
		log.info( "Getting events from EventBriteAPI for city {} Successful", cityName );
		return CompletableFuture.completedFuture(eventsForGivenCity);
	}

	@Async
	public CompletableFuture<List<EventEventFulAPI>> getEventsListFromEventFulAPI( String cityName ,List<CategoryEventFul> categories)
	{
		log.info( "Getting events from EventFulAPI for city {}", cityName );
		//List of events for city
		List<EventEventFulAPI> eventsForGivenCity = new ArrayList<>(  );
		// Get events for the given city for each category and get a single list finally
		for(CategoryEventFul category : categories)
		{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept( Collections.singletonList( MediaType.APPLICATION_XML ) );
			HttpEntity<String> entity = new HttpEntity<>( WORD_PARAMETERS, headers );
			String authToken = environment.getProperty( "eventapi.eventful.token" );

			ResponseEntity<ResponseEventFulAPI> initialResponseEntity = restTemplate.exchange(
					"http://api.eventful.com/rest/events/search?app_key=" + authToken + "&location=" + cityName
							+ "&sort_order=date&category=" + category.getId() + "&page_number=1", HttpMethod.GET,
					entity, ResponseEventFulAPI.class );
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
			// Add the city name and the category name to the events
			events.replaceAll( event -> {
				event.setCity( cityName );
				event.setCategory( category.getName() );
				return event;
			} );
			// Add the events for the category to the main list
			eventsForGivenCity.addAll(events);
		}
		log.info( "Getting events from EventFulAPI for city {} Successful", cityName );
		return CompletableFuture.completedFuture(eventsForGivenCity);
	}


	public List<CategoryEventBrite> getCatagotyListmEventBriteAPI()
	{
		log.info( "Getting category list from EventBriteAPI for " );
		String authToken = environment.getProperty( "eventapi.eventbrite.token" );

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON ) );
		headers.add( "User-Agent", "Spring's RestTemplate" );  // value can be whatever
		headers.add( "Authorization", "Bearer " + authToken );

		ResponseEntity<CategoryResponseEventBrite> responseEntity = restTemplate.exchange(
				"https://www.eventbriteapi.com/v3/categories/",
				HttpMethod.GET,
				new HttpEntity<>( WORD_PARAMETERS, headers ),
				CategoryResponseEventBrite.class
		);
		CategoryResponseEventBrite response = responseEntity.getBody();
		return response.getCategories();
	}


	public List<CategoryEventFul> getCatagotyListmEventFulAPI()
	{
		log.info( "Getting categories from EventFulAPI " );
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept( Collections.singletonList( MediaType.APPLICATION_XML ) );
		HttpEntity<String> entity = new HttpEntity<>( WORD_PARAMETERS, headers );
		String authToken = environment.getProperty( "eventapi.eventful.token" );

		ResponseEntity<CategoryResponseEventFul> response = restTemplate.exchange(
				"http://api.eventful.com/rest/categories/list?app_key="+authToken,
				HttpMethod.GET,
				new HttpEntity<>( WORD_PARAMETERS, headers ),
				new ParameterizedTypeReference<CategoryResponseEventFul>(){});
		return response.getBody().getCategories();
	}
}
