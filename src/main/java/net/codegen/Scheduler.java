package net.codegen;

import net.codegen.models.Event;
import net.codegen.models.event_brite_api.EventEventBriteAPI;
import net.codegen.models.event_ful_api.EventEventFulAPI;
import net.codegen.services.APIClient;
import net.codegen.services.EventSaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Scheduler
{
	@Autowired
	private EventSaveService eventSaveService;
	@Autowired
	private APIClient apiClient;

	//Full event List for a single save operation
	private static List<Event> eventList = null;

	// static method to create instance of Singleton class
	//Singleton pattern used for eventlist as same object can be used again and again.
	private static List<Event> getEventList()
	{
		if ( eventList == null )
			eventList = new ArrayList<>();
		return eventList;
	}

	private static final Logger log = LoggerFactory.getLogger( Scheduler.class );

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
		List<EventEventBriteAPI> eventListFromEventBriteAPIForMelbourne = apiClient.getEventsListFromEventBriteAPI( 0 );
		List<EventEventBriteAPI> eventListFromEventBriteAPIForBrisbane = apiClient.getEventsListFromEventBriteAPI( 1 );
		List<EventEventFulAPI> eventsListFromEventFulAPIAPIForMelbourne = apiClient.getEventsListFromEventFulAPI( 0 );
		List<EventEventFulAPI> eventsListFromEventFulAPIAPIForBrisbane = apiClient.getEventsListFromEventFulAPI( 1 );
		eventList.addAll( eventListFromEventBriteAPIForMelbourne );
		eventList.addAll( eventListFromEventBriteAPIForBrisbane );
		eventList.addAll( eventsListFromEventFulAPIAPIForMelbourne );
		eventList.addAll( eventsListFromEventFulAPIAPIForBrisbane );
		eventSaveService.insertEvent( eventList );
		log.info( "Successfully updated the database" );
	}




}
