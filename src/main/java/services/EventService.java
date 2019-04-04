package services;

import models.EventEventBriteAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.EventRepository;

@Service
public class EventService
{
	@Autowired
	private EventRepository eventRepository;

	public EventEventBriteAPI insertEvent( EventEventBriteAPI event )
	{
		return eventRepository.save( event );
	}
}
