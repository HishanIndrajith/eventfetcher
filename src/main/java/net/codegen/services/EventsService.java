package net.codegen.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codegen.models.Event;
import net.codegen.repositories.EventsRepository;

@Service
public class EventsService
{
	private static final Logger log = LoggerFactory.getLogger( EventsService.class );
	@Autowired
	private EventsRepository eventsRepository;

	public boolean insertEventList( List<Event> events )
	{
		boolean isSaveSuccessful = true;
		try
		{
			eventsRepository.saveAll( events );
		}
		catch ( Exception e )
		{
			isSaveSuccessful = false;
			log.error( "{}", e );
		}
		return isSaveSuccessful;
	}

	public String getLastUpdatedTime( String categoryName, String city )
	{
		String lastUpdatedTime = eventsRepository.getLastUpdatedTime( categoryName, city );
		if(lastUpdatedTime==null)
			return "1000-01-01T00:00:00Z";
		else
			return eventsRepository.getLastUpdatedTime( categoryName, city );
	}
}
