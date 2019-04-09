package net.codegen.services;

import net.codegen.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.codegen.repositories.EventRepository;

import java.util.List;

@Service
public class EventService
{
	@Autowired
	private EventRepository eventRepository;

	public boolean insertEvent( List<Event> events )
	{
		eventRepository.saveAll( events );
		return true;
	}
}
