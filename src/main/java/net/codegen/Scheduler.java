package net.codegen;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.codegen.models.Event;
import net.codegen.models.event_brite_api.CategoryEventBrite;
import net.codegen.models.event_brite_api.EventEventBriteAPI;
import net.codegen.services.APIClient;
import net.codegen.services.CityListFetcher;
import net.codegen.services.EventSaveService;

@Component
public class Scheduler
{
	// Logger instance
	private static final Logger log = LoggerFactory.getLogger( Scheduler.class );
	// Initializing events array List
	private static List<Event> eventList;

	static
	{
		// implement the array list
		eventList = new ArrayList<>();
	}

	// Injecting the required services
	@Autowired
	private EventSaveService eventSaveService;
	@Autowired
	private APIClient apiClient;
	@Autowired
	private CityListFetcher cityListFetcher;

	/*
	 * https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html The pattern is a list of
	 * six single space-separated fields: representing second, minute, hour, day, month, weekday Cron Sequences used "0 * * * * *" - 0th second of each
	 * minute "0 0 10 * * *" - 10th hour of each day "0 0 10 1 * *" - 10th hour of first day of each month
	 */
	@Scheduled(cron = "* * * * * *")
	public void doSheduledTask()
	{
		log.info( "starting the scheduled task" );
		// Retrieve the list of cities from the json file and assign to an ArrayList
		List<String> cityList = cityListFetcher.getRequiredCityList();
		// Retrieve the Event category list from the API - EventBriteAPI, and assign to an ArrayList
		List<CategoryEventBrite> categoriesEventBrite = apiClient.getCatagotyListmEventBriteAPI();

		if ( !cityList.isEmpty() && !categoriesEventBrite.isEmpty() )
		{
			// Iterate the cities
			for ( String cityName : cityList )
			{
				// Retrieve the EventBrite API event list for the city by giving city name and category list, and assign to an ArrayList
				List<EventEventBriteAPI> eventEventBrite = apiClient.getEventsListFromEventBriteAPI( cityName,
						categoriesEventBrite );
				// Add the EventBrite API event list to the parent event list.
				eventList.addAll( eventEventBrite );
				if ( !eventList.isEmpty() )
				{
					// Save the event list for the city in database
					if ( eventSaveService.insertEvent( eventList ) )
						log.info( "Successfully updated the database for city {}", cityName );
					else
						log.error( "Failed to save to database for city {}", cityName );
					// Clear the event list for next city.
					eventList.clear();
				}
				else
				{
					log.error( "No data to save to database for city {}", cityName );
				}
			}
		}
		else
		{
			log.error( "City List not available or Category list cannot be retrieved" );
		}

	}

}
