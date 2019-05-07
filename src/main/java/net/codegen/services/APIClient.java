package net.codegen.services;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import net.codegen.models.event_brite_api.CategoryEventBrite;
import net.codegen.models.event_brite_api.CategoryResponseEventBrite;
import net.codegen.models.event_brite_api.EventEventBriteAPI;
import net.codegen.models.event_brite_api.ResponseEventBriteAPI;
import net.codegen.models.event_ful_api.CategoryEventFul;
import net.codegen.models.event_ful_api.CategoryResponseEventFul;
import net.codegen.models.event_ful_api.EventEventFulAPI;
import net.codegen.models.event_ful_api.ResponseEventFulAPI;

/**
 * This class provides the functions to do needed tasks using the APIs eventbrite and eventful. Getting event list for a given city and list of
 * categories from EventBrite API - <b>getEventsListFromEventBriteAPI</b> Getting event list for a given city and list of categories from EventFul API
 * - <b>getEventsListFromEventFulAPI</b> Getting event category list for EventBrite API - <b>getCatagotyListmEventBriteAPI</b> Getting event category
 * list for Eventful API - <b>getCatagotyListmEventFulAPI</b>
 */

@Service
public class APIClient
{
	private static final Logger log = LoggerFactory.getLogger( APIClient.class );
	private static final String WORD_PARAMETERS = "parameters";
	private static final int RATE_LIMIT_EVENTBRITE = 1000;
	private static final long MILISECONDS_FOR_HOUR = 3600000;
	// Injecting the required instances for API Client
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;
	@Autowired
	private EventsService eventsService;
	/*
	 * Date variable that keeps the starting time of an unit hour. This is assigned with current time when starting the application, 1000 api call limit
	 * reached or 1 hour has passed from the time in this variable.
	 */
	private Date startTime;
	// This is the variable that keep the current API calls in the unit hour.
	private int callCount = 0;

	// This function returns a Array List of event objects in the given city name with the category specified,
	// for given list of categories from EventBrite API
	public List<EventEventBriteAPI> getEventsListFromEventBriteAPI( String cityName,
			List<CategoryEventBrite> categories )
	{
		log.info( "Getting events from EventBriteAPI for city {}", cityName );
		// List of events for city
		List<EventEventBriteAPI> eventsForGivenCity = new ArrayList<>();
		try
		{
			// Get events for the given city for each category and get a single list
			for ( CategoryEventBrite category : categories )
			{
				// Get the eventbrite API token (Token is registered to hishan.codegen@gmail.com)
				String authToken = environment.getProperty( "eventapi.eventbrite.token" );
				// Create the HTTP Headers required for the API call
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON ) );
				headers.add( "User-Agent", "Spring's RestTemplate" ); // value can be whatever
				headers.add( "Authorization", "Bearer " + authToken );

				// Get the first API response for the given parameters using rest template
				ResponseEntity<ResponseEventBriteAPI> initialResponseEntity = restTemplate.exchange(
						"https://www.eventbriteapi.com/v3/events/search?categories=" + category.getId()
								+ "&location.address=" + cityName + "&expand=venue&page=1",
						HttpMethod.GET, new HttpEntity<>( WORD_PARAMETERS, headers ), ResponseEventBriteAPI.class );
				ResponseEventBriteAPI initialResponse = initialResponseEntity.getBody();
				// Increment the call count
				callCount++;
				// Get the page count of the Response needed to get all paginated data.
				int pageCount = initialResponse.getPagination().getPageCount();
				// Get the list of events from initial response. The next events from other pages are added to this list.
				List<EventEventBriteAPI> events = initialResponse.getEvents();

				// Iterate to get all events from all pages.
				for ( int i = 1; i < pageCount; i++ )
				{
					// Execute the logic to check whether api rate limit is near to reach and take steps to sleep.
					runUnitHourLogic();
					log.info( "Getting events from EventBriteAPI for city {} for category {} - progress {} / {}",
							cityName, category.getName(), i, pageCount );
					// Get the event response from the API for the relevant page number.
					ResponseEntity<ResponseEventBriteAPI> responseEntity = restTemplate.exchange(
							"https://www.eventbriteapi.com/v3/events/search?location.address=" + cityName
									+ "&expand=venue&page=" + ( i + 1 ),
							HttpMethod.GET, new HttpEntity<>( WORD_PARAMETERS, headers ), ResponseEventBriteAPI.class );
					// Get the response out of ResponseEntity
					ResponseEventBriteAPI response = responseEntity.getBody();
					// Increment the call count
					callCount++;
					// Get the list of events from the response
					List<EventEventBriteAPI> nextEventsList = response.getEvents();
					// Add event list for the page to the main array list of events for the city and category
					events.addAll( nextEventsList );
				}
				// Add the events for the category to the main list
				customAddAllEventsToAList( eventsForGivenCity, events, cityName, category );
			}
			log.info( "Getting events from EventBriteAPI for city {} Successful", cityName );
		}
		catch ( RestClientException e )
		{
			log.error( "Error in Rest Client in getting data from API {}", e );
		}
		return eventsForGivenCity;
	}

	// This function returns a Array List of event objects in the given city name with the category specified,
	// for given list of categories from EventFul API
	public List<EventEventFulAPI> getEventsListFromEventFulAPI( String cityName, List<CategoryEventFul> categories )
	{
		log.info( "Getting events from EventFulAPI for city {}", cityName );
		List<EventEventFulAPI> eventsForGivenCity = null;
		try
		{
			// List of events for city
			eventsForGivenCity = new ArrayList<>();
			// Get the eventFul API token (Token is registered to hishan.codegen@gmail.com)
			String authToken = environment.getProperty( "eventapi.eventful.token" );
			// Create the HTTP Headers required for the API call
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept( Collections.singletonList( MediaType.APPLICATION_XML ) );
			// Get events for the given city for each category and get a single list finally
			for ( CategoryEventFul category : categories )
			{
				HttpEntity<String> entity = new HttpEntity<>( WORD_PARAMETERS, headers );
				// Get the first API response for the given parameters using rest template
				ResponseEntity<ResponseEventFulAPI> initialResponseEntity = restTemplate.exchange(
						"http://api.eventful.com/rest/events/search?app_key=" + authToken + "&location=" + cityName
								+ "&sort_order=date&category=" + category.getId() + "&page_number=1",
						HttpMethod.GET, entity, ResponseEventFulAPI.class );
				ResponseEventFulAPI initialResponse = initialResponseEntity.getBody();
				// Get the page count of the Response needed to get all paginated data.
				int pageCount = Integer.parseInt( initialResponse.getPageCount() );
				// Get the list of events from initial response. The next events from other pages are added to this list.
				List<EventEventFulAPI> events = initialResponse.getEvents();
				// Iterate to get all events from all pages.
				for ( int i = 1; i < pageCount; i++ )
				{
					ResponseEntity<ResponseEventFulAPI> responseEntity = restTemplate.exchange(
							"http://api.eventful.com/rest/events/search?app_key=pzZR48qPz4pSzNQN&location=" + cityName
									+ "&sort_order=date&page_number=" + ( i + 1 ),
							HttpMethod.GET, new HttpEntity<>( WORD_PARAMETERS, headers ), ResponseEventFulAPI.class );
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
				eventsForGivenCity.addAll( events );
			}
			log.info( "Getting events from EventFulAPI for city {} Successful", cityName );
		}
		catch ( RestClientException e )
		{
			log.error( "Error in Rest Client in getting data from API {}", e );
		}
		catch ( NumberFormatException e )
		{
			log.error( "{}", e );
		}
		return eventsForGivenCity;
	}

	public List<CategoryEventBrite> getCatagotyListmEventBriteAPI()
	{
		CategoryResponseEventBrite response = null;
		try
		{
			// Assign current time to startTime as this method is called at the beginning of the application.
			startTime = new Date();
			// Get eventbrite token
			String authToken = environment.getProperty( "eventapi.eventbrite.token" );
			// Create headers
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON ) );
			headers.add( "User-Agent", "Spring's RestTemplate" ); // value can be whatever
			headers.add( "Authorization", "Bearer " + authToken );
			// Make the API call
			ResponseEntity<CategoryResponseEventBrite> responseEntity = restTemplate.exchange(
					"https://www.eventbriteapi.com/v3/categories/", HttpMethod.GET,
					new HttpEntity<>( WORD_PARAMETERS, headers ), CategoryResponseEventBrite.class );
			// Increment call count
			callCount++;
			// Get and return the event category list.
			response = responseEntity.getBody();
		}
		catch ( RestClientException e )
		{
			log.error( "{}", e );
		}

		if ( response == null )
			return Collections.emptyList();
		else
			return response.getCategories();

	}

	public List<CategoryEventFul> getCatagotyListmEventFulAPI()
	{
		ResponseEntity<CategoryResponseEventFul> response = null;
		try
		{
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept( Collections.singletonList( MediaType.APPLICATION_XML ) );
			String authToken = environment.getProperty( "eventapi.eventful.token" );

			response = restTemplate.exchange( "http://api.eventful.com/rest/categories/list?app_key=" + authToken,
					HttpMethod.GET, new HttpEntity<>( WORD_PARAMETERS, headers ),
					new ParameterizedTypeReference<CategoryResponseEventFul>()
					{
					} );
		}
		catch ( RestClientException e )
		{
			log.error( "{}", e );
		}
		if ( response == null )
			return Collections.emptyList();
		else
			return response.getBody().getCategories();
	}

	private void runUnitHourLogic()
	{
		/*
		 * This function check the variables start time , and the api calls count and takes necessary actions whether to sleep for some time or start a new
		 * unit hour. Unit hours are the time gaps of one hour starting from the application starting time.
		 */
		// Get the current time.
		Date currentTime = new Date();
		// Get the time gap in milliseconds from starting of the unit hour.
		long milisecondsFromStart = currentTime.getTime() - startTime.getTime();
		if ( callCount >= RATE_LIMIT_EVENTBRITE - 20 )
		{
			/*
			 * This block is executed if api call count limit has nearly reached. Rate limit is 1000 api calls per hour. ->
			 * https://www.eventbrite.com/platform/api#/introduction/errors/common-errors But it is mentioned as 2000 here ->
			 * https://www.eventbrite.com/platform/docs/rate-limits Need to check it.
			 */
			try
			{
				log.info( "Sleeping  {} miliseconds", ( MILISECONDS_FOR_HOUR - milisecondsFromStart ) );
				// The application sleeps until an hour completes from the starting time of the unit hour to execute next api call.
				Thread.sleep( MILISECONDS_FOR_HOUR - milisecondsFromStart );
				// The call count and the start time are initialized to default values to start a new unit hour.
				callCount = 0;
				startTime = currentTime;
			}
			catch ( InterruptedException e )
			{
				log.info( "Exception in sleeping {}", e );
				Thread.currentThread().interrupt();
			}
		}
		if ( milisecondsFromStart >= MILISECONDS_FOR_HOUR )
		{
			// This block is executed if 1 hour has passed before api call count become 1000
			// Then a new unit hour is started by making the call count and startTime default values.
			callCount = 0;
			startTime = currentTime;

		}
	}

	// This is because addAll in ArrayList cannot be used as updatedDate must be compared event wise before adding.
	private void customAddAllEventsToAList( List<EventEventBriteAPI> mainList, List<EventEventBriteAPI> listToAdd,
			String cityName, CategoryEventBrite category )
	{
		String timeCityAndCategoryUpdatedEarly = eventsService.getLastUpdatedTime( category.getName(), cityName );
		Instant instantCityAndCategoryUpdatedEarly = Instant.parse( timeCityAndCategoryUpdatedEarly );
		String currentTime = DateTimeFormatter.ISO_INSTANT.format( Instant.now() );
		for ( EventEventBriteAPI event : listToAdd )
		{
			String publishedDate = event.getPublishedDate();
			if(publishedDate!=null){
				Instant eventPublishedTime = Instant.parse( event.getPublishedDate() );
				if ( instantCityAndCategoryUpdatedEarly.isBefore( eventPublishedTime ) )
				{
					event.setCity( cityName );
					event.setCategory( category.getName() );
					event.setUpdatedTime( currentTime );
					mainList.add( event );
				}
			}

		}
	}
}
