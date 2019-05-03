package net.codegen.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.codegen.models.Event;
import net.codegen.repositories.EventRepository;

@Service
public class EventSaveService
{
	private static final Logger log = LoggerFactory.getLogger( EventSaveService.class );
	@Autowired
	private EventRepository eventRepository;

	public boolean insertEvent( List<Event> events )
	{
		boolean isSaveSuccessful = true;
		try
		{
			eventRepository.saveAll( events );
		}
		catch ( Exception e )
		{
			isSaveSuccessful = false;
			log.error( "{}",e );
		}
		return isSaveSuccessful;
	}
}
